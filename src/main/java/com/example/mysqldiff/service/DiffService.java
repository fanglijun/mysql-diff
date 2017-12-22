package com.example.mysqldiff.service;

import com.example.mysqldiff.config.DatabaseConfig;
import com.example.mysqldiff.mapper.ColumnMapper;
import com.example.mysqldiff.mapper.TableMapper;
import com.example.mysqldiff.po.ColumnPO;
import com.example.mysqldiff.po.TablePO;
import com.example.mysqldiff.util.MapUtils;
import com.example.mysqldiff.vo.DiffTypeEnum;
import com.example.mysqldiff.vo.ColumnDiffVo;
import com.example.mysqldiff.vo.TableDiffVo;
import javafx.scene.control.TablePosition;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        for (Map.Entry<String, ColumnPO> entry : table1ColumnMap.entrySet()) {
            String columnName = entry.getKey();
            String col1Type = this.formatColumnType(entry.getValue());
            ColumnPO col2 = table2ColumnMap.get(columnName);
            if (null == col2) {
                diffVos.add(new ColumnDiffVo(columnName, DiffTypeEnum.ADDED, col1Type, null));
            } else {
                String col2Type = this.formatColumnType(col2);
                if (!col1Type.equals(col2Type)) {
                    diffVos.add(new ColumnDiffVo(columnName, DiffTypeEnum.CHANGED, col1Type, col2Type));
                }
            }
        }
        for (Map.Entry<String, ColumnPO> entry : table2ColumnMap.entrySet()) {
            String columnName = entry.getKey();
            String col2Type = this.formatColumnType(entry.getValue());
            if (null == table1ColumnMap.get(columnName)) {
                diffVos.add(new ColumnDiffVo(columnName, DiffTypeEnum.DELETED, null, col2Type));
            }
        }
        return diffVos;
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
