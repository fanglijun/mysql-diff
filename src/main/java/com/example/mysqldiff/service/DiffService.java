package com.example.mysqldiff.service;

import com.example.mysqldiff.config.DatabaseConfig;
import com.example.mysqldiff.mapper.ColumnMapper;
import com.example.mysqldiff.mapper.TableMapper;
import com.example.mysqldiff.po.ColumnPO;
import com.example.mysqldiff.po.TablePO;
import com.example.mysqldiff.util.MapUtils;
import com.example.mysqldiff.vo.DiffTypeEnum;
import com.example.mysqldiff.vo.ColumnDiffVo;
import com.example.mysqldiff.vo.SyncDirectionEnum;
import com.example.mysqldiff.vo.TableDiffVo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.*;

@Service
public class DiffService {

    @Resource
    private ColumnMapper columnMapper;
    @Resource
    private TableMapper tableMapper;
    @Resource
    private DatabaseConfig databaseConfig;

    /**
     * 对比数据库里的所有表
     */
    public List<TableDiffVo> compareDb() {
        DatabaseConfig.switchDatasource(DatabaseConfig.DATASOURCE_1);
        List<TablePO> db1Tables = tableMapper.selectByDbName(databaseConfig.getDs1Db());
        DatabaseConfig.switchDatasource(DatabaseConfig.DATASOURCE_2);
        List<TablePO> db2Tables = tableMapper.selectByDbName(databaseConfig.getDs2Db());
        Map<String, TablePO> db1TablesMap = MapUtils.listToMap(db1Tables, TablePO::getTableName);
        Map<String, TablePO> db2TablesMap = MapUtils.listToMap(db2Tables, TablePO::getTableName);

        List<TableDiffVo> diffVos = new ArrayList<>();
        for (Map.Entry<String, TablePO> entry : db1TablesMap.entrySet()) {
            String tableName = entry.getKey();
            TablePO table2 = db2TablesMap.get(tableName);
            if (null == table2) {
                diffVos.add(new TableDiffVo(tableName, DiffTypeEnum.ADDED, null));
            } else {
                List<ColumnDiffVo> columnDiffVos = this.compareTable(tableName, tableName);
                if (columnDiffVos.size() > 0) {
                    diffVos.add(new TableDiffVo(tableName, DiffTypeEnum.CHANGED, columnDiffVos));
                }
            }
        }
        for (Map.Entry<String, TablePO> entry : db2TablesMap.entrySet()) {
            String tableName = entry.getKey();
            if (null == db1TablesMap.get(tableName)) {
                diffVos.add(new TableDiffVo(tableName, DiffTypeEnum.DELETED, null));
            }
        }
        return diffVos;
    }

    /**
     * 对比两个表，两个表都要存在
     */
    public List<ColumnDiffVo> compareTable(String table1, String table2) {
        DatabaseConfig.switchDatasource(DatabaseConfig.DATASOURCE_1);
        List<ColumnPO> table1Columns = columnMapper.selectByTableName(databaseConfig.getDs1Db(), table1);
        DatabaseConfig.switchDatasource(DatabaseConfig.DATASOURCE_2);
        List<ColumnPO> table2Columns = columnMapper.selectByTableName(databaseConfig.getDs2Db(), table2);
        Map<String, ColumnPO> table1ColumnMap = MapUtils.listToMap(table1Columns, ColumnPO::getColumnName);
        Map<String, ColumnPO> table2ColumnMap = MapUtils.listToMap(table2Columns, ColumnPO::getColumnName);

        List<ColumnDiffVo> diffVos = new ArrayList<>();
        String prevColumnName = null;
        for (Map.Entry<String, ColumnPO> entry : table1ColumnMap.entrySet()) {
            String columnName = entry.getKey();
            String col1Type = this.formatColumnType(entry.getValue());
            BigInteger position = entry.getValue().getOrdinalPosition();
            ColumnPO col2 = table2ColumnMap.get(columnName);
            if (null == col2) {
                diffVos.add(new ColumnDiffVo(columnName, DiffTypeEnum.ADDED, col1Type, null, position, prevColumnName));
            } else {
                String col2Type = this.formatColumnType(col2);
                if (!col1Type.equals(col2Type)) {
                    diffVos.add(new ColumnDiffVo(columnName, DiffTypeEnum.CHANGED, col1Type, col2Type, position, prevColumnName));
                }
            }
            prevColumnName = columnName;
        }
        // 上一个共有的列的序号，
        prevColumnName = null;
        for (Map.Entry<String, ColumnPO> entry : table2ColumnMap.entrySet()) {
            String columnName = entry.getKey();
            String col2Type = this.formatColumnType(entry.getValue());
            BigInteger position = entry.getValue().getOrdinalPosition();
            if (null == table1ColumnMap.get(columnName)) {
                diffVos.add(new ColumnDiffVo(columnName, DiffTypeEnum.DELETED, null, col2Type, position, prevColumnName));
            }
            prevColumnName = columnName;
        }

        diffVos.sort(Comparator.comparing(ColumnDiffVo::getOrdinalPosition));
        return diffVos;
    }

    /**
     * 生成所有表的同步SQL
     */
    public Map<String, List<String>> syncDb(SyncDirectionEnum syncDirection) {
        List<TableDiffVo> tableDiffVos = compareDb();
        Map<String, List<String>> sqls = new LinkedHashMap<>();
        if (syncDirection.equals(SyncDirectionEnum.DB2_TO_DB1)) {
            for (TableDiffVo tableDiffVo : tableDiffVos) {
                String tableName = tableDiffVo.getTableName();
                if (tableDiffVo.getDiffType().equals(DiffTypeEnum.ADDED)) {
                    String sql = "DROP TABLE `" + tableDiffVo.getTableName() + "`;";
                    sqls.put(tableName, Collections.singletonList(sql));
                    continue;
                }
                if (tableDiffVo.getDiffType().equals(DiffTypeEnum.DELETED)) {
                    String sql = this.showCreateTable(null, tableName);
                    sqls.put(tableName, Collections.singletonList(sql));
                    continue;
                }
                if (tableDiffVo.getDiffType().equals(DiffTypeEnum.CHANGED)) {
                    sqls.put(tableName, this.syncTable(tableName, tableDiffVo.getColumnDiffs(), syncDirection));
                }
            }
        } else if (syncDirection.equals(SyncDirectionEnum.DB1_TO_DB2)) {
            for (TableDiffVo tableDiffVo : tableDiffVos) {
                String tableName = tableDiffVo.getTableName();
                if (tableDiffVo.getDiffType().equals(DiffTypeEnum.DELETED)) {
                    String sql = "DROP TABLE `" + tableDiffVo.getTableName() + "`;";
                    sqls.put(tableName, Collections.singletonList(sql));
                    continue;
                }
                if (tableDiffVo.getDiffType().equals(DiffTypeEnum.ADDED)) {
                    String sql = this.showCreateTable(null, tableName);
                    sqls.put(tableName, Collections.singletonList(sql));
                    continue;
                }
                if (tableDiffVo.getDiffType().equals(DiffTypeEnum.CHANGED)) {
                    sqls.put(tableName, this.syncTable(tableName, tableDiffVo.getColumnDiffs(), syncDirection));
                }
            }
        }
        return sqls;
    }

    /**
     * 获取表创建SQL，table1 和 table2只能有一个不为null
     */
    public String showCreateTable(String table1, String table2) {
        if (table1 != null) {
            DatabaseConfig.switchDatasource(DatabaseConfig.DATASOURCE_1);
            Map<String, String> result = tableMapper.showCreateTable(databaseConfig.getDs1Db(), table1);
            return result.get("Create Table");
        } else if (table2 != null) {
            DatabaseConfig.switchDatasource(DatabaseConfig.DATASOURCE_2);
            Map<String, String> result = tableMapper.showCreateTable(databaseConfig.getDs2Db(), table2);
            return result.get("Create Table");
        } else {
            return null;
        }
    }

    /**
     * 生成同步SQL语句
     * Note：不会覆盖目标字段的注释
     */
    public List<String> syncTable(String tableName, List<ColumnDiffVo> columnDiffVos, SyncDirectionEnum syncDirection) {
        // 读取目标表的字段，目的是读取字段注释，避免同步时注释被覆盖
        List<ColumnPO> columnPOS;
        if (syncDirection.equals(SyncDirectionEnum.DB2_TO_DB1)) {
            DatabaseConfig.switchDatasource(DatabaseConfig.DATASOURCE_1);
            columnPOS = columnMapper.selectByTableName(databaseConfig.getDs1Db(), tableName);
        } else {
            DatabaseConfig.switchDatasource(DatabaseConfig.DATASOURCE_2);
            columnPOS = columnMapper.selectByTableName(databaseConfig.getDs2Db(), tableName);
        }
        Map<String, ColumnPO> columnPOMap = MapUtils.listToMap(columnPOS, ColumnPO::getColumnName);

        List<String> sqls = new ArrayList<>();
        // 同步到DB1
        if (syncDirection.equals(SyncDirectionEnum.DB2_TO_DB1)) {
            for (ColumnDiffVo diffVo : columnDiffVos) {
                String action = null;
                if (diffVo.getDiffType().equals(DiffTypeEnum.ADDED)) {
                    String sql = "ALTER TABLE `" + tableName + "` DROP COLUMN `" + diffVo.getColumnName() + "`;";
                    sqls.add(sql);
                    continue;
                }
                if (diffVo.getDiffType().equals(DiffTypeEnum.DELETED)) {
                    action = "ADD";
                } else if (diffVo.getDiffType().equals(DiffTypeEnum.CHANGED)) {
                    action = "MODIFY";
                }
                if (null != action) {
                    String sql = "ALTER TABLE `" + tableName + "` " + action + " COLUMN `" + diffVo.getColumnName() + "` " + diffVo.getCol2Type();
                    if (action.equals("MODIFY")) {
                        // 取出目标字段的注释
                        ColumnPO columnPO = columnPOMap.get(diffVo.getColumnName());
                        if (!StringUtils.isEmpty(columnPO.getColumnComment())) {
                            sql += " COMMENT '" + columnPO.getColumnComment() + "'";
                        }
                    }
                    sql += ";";
                    sqls.add(sql);
                }
            }
        } else if (syncDirection.equals(SyncDirectionEnum.DB1_TO_DB2)) {
            for (ColumnDiffVo diffVo : columnDiffVos) {
                String action = null;
                if (diffVo.getDiffType().equals(DiffTypeEnum.DELETED)) {
                    String sql = "ALTER TABLE `" + tableName + "` DROP COLUMN `" + diffVo.getColumnName() + "`;";
                    sqls.add(sql);
                    continue;
                }
                if (diffVo.getDiffType().equals(DiffTypeEnum.ADDED)) {
                    action = "ADD";
                } else if (diffVo.getDiffType().equals(DiffTypeEnum.CHANGED)) {
                    action = "MODIFY";
                }
                if (null != action) {
                    String sql = "ALTER TABLE `" + tableName + "` " + action + " COLUMN `" + diffVo.getColumnName() + "` " + diffVo.getCol1Type();
                    if (action.equals("MODIFY")) {
                        // 取出目标字段的注释
                        ColumnPO columnPO = columnPOMap.get(diffVo.getColumnName());
                        if (!StringUtils.isEmpty(columnPO.getColumnComment())) {
                            sql += " COMMENT '" + columnPO.getColumnComment() + "'";
                        }
                    }
                    sql += ";";
                    sqls.add(sql);
                }
            }
        }
        return sqls;
    }

    private String formatColumnType(ColumnPO columnPO) {
        StringBuilder stringBuilder = new StringBuilder();
        // column type
        stringBuilder.append(columnPO.getColumnType());

        // charset
        if (null != columnPO.getCharacterSetName()) {
            stringBuilder.append(" CHARACTER SET ").append(columnPO.getCharacterSetName());
            stringBuilder.append(" COLLATE ").append(columnPO.getCollationName());
        }

        // nullable
        boolean isNullable = "YES".equalsIgnoreCase(columnPO.getIsNullable());
        if (!isNullable) {
            stringBuilder.append(" NOT NULL ");
        }

        // default value
        String defaultValue = columnPO.getColumnDefault();
        if (null != defaultValue) {
            if ("CURRENT_TIMESTAMP".equalsIgnoreCase(defaultValue)) {
                stringBuilder.append(" DEFAULT CURRENT_TIMESTAMP");
            } else {
                stringBuilder.append(" DEFAULT '").append(defaultValue).append("'");
            }
        } else {
            // 默认值是null时，仅当字段isNullable时才加上default null，i.e. 字段是not null时，后面就不加default null了
            if (isNullable) {
                stringBuilder.append(" DEFAULT NULL");
            }
        }

        // extra
        String extra = columnPO.getExtra();
        if ("auto_increment".equalsIgnoreCase(extra)) {
            stringBuilder.append(" AUTO_INCREMENT");
        } else if ("on update CURRENT_TIMESTAMP".equalsIgnoreCase(extra)) {
            stringBuilder.append(" ON UPDATE CURRENT_TIMESTAMP");
        }

        return stringBuilder.toString();
    }
}
