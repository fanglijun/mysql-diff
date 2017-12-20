package com.example.mysqldiff.mapper;

import com.example.mysqldiff.po.ColumnPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ColumnMapper {

    @Select("SELECT * FROM information_schema.columns WHERE table_schema=#{dbName} AND table_name=#{tableName} ORDER BY ordinal_position")
    List<ColumnPO> selectByTableName(@Param("dbName") String dbName, @Param("tableName") String tableName);

}
