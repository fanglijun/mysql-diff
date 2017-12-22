package com.example.mysqldiff.mapper;

import com.example.mysqldiff.po.TablePO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 对应表 TABLES
 */
@Mapper
public interface TableMapper {

    @Select("SELECT * FROM information_schema.tables WHERE table_schema=#{dbName} ORDER BY table_name")
    List<TablePO> selectByDbName(@Param("dbName") String dbName);

    @Select("SHOW CREATE TABLE `${dbName}`.`${tableName}`")
    Map<String, String> showCreateTable(@Param("dbName") String dbName, @Param("tableName") String tableName);
}
