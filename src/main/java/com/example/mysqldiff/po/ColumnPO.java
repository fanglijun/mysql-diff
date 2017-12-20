package com.example.mysqldiff.po;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * 对应表 COLUMNS
 */
public class ColumnPO implements Serializable {
    private String tableCatalog; // 与SQL标准兼容，在MySQL里没有用
    private String tableSchema; // 数据库名
    private String tableName; // 表名
    private String columnName; // 字段名
    private BigInteger ordinalPosition; // 字段在表里的顺序
    private String columnDefault; // 默认值
    private String isNullable; // 是否允许为NULL
    private String dataType; // 数据类型
    private BigInteger characterMaximumLength; // 最大字符个数
    private BigInteger characterOctetLength; // 最大字符长度 bytes；utf8一个字符占3个byte，utf8mb4占4个
    private BigInteger numericPrecision; // 数字类型整数部分长度
    private BigInteger numericScale; // 数字类型小数位数
    private BigInteger datetimePrecision; // 日期类型精度
    private String characterSetName; // 字符集名称
    private String collationName; // 字符排序规则
    private String columnType; // 字段类型
    private String columnKey; // 索引类型：PRI，UNI，MUL；MUL表示一般索引，即允许值重复的索引
    private String extra; // 其他信息
    private String privileges;
    private String columnComment; // 注释

    public String getTableCatalog() {
        return tableCatalog;
    }

    public void setTableCatalog(String tableCatalog) {
        this.tableCatalog = tableCatalog;
    }

    public String getTableSchema() {
        return tableSchema;
    }

    public void setTableSchema(String tableSchema) {
        this.tableSchema = tableSchema;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public BigInteger getOrdinalPosition() {
        return ordinalPosition;
    }

    public void setOrdinalPosition(BigInteger ordinalPosition) {
        this.ordinalPosition = ordinalPosition;
    }

    public String getColumnDefault() {
        return columnDefault;
    }

    public void setColumnDefault(String columnDefault) {
        this.columnDefault = columnDefault;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public void setIsNullable(String isNullable) {
        this.isNullable = isNullable;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public BigInteger getCharacterMaximumLength() {
        return characterMaximumLength;
    }

    public void setCharacterMaximumLength(BigInteger characterMaximumLength) {
        this.characterMaximumLength = characterMaximumLength;
    }

    public BigInteger getCharacterOctetLength() {
        return characterOctetLength;
    }

    public void setCharacterOctetLength(BigInteger characterOctetLength) {
        this.characterOctetLength = characterOctetLength;
    }

    public BigInteger getNumericPrecision() {
        return numericPrecision;
    }

    public void setNumericPrecision(BigInteger numericPrecision) {
        this.numericPrecision = numericPrecision;
    }

    public BigInteger getNumericScale() {
        return numericScale;
    }

    public void setNumericScale(BigInteger numericScale) {
        this.numericScale = numericScale;
    }

    public BigInteger getDatetimePrecision() {
        return datetimePrecision;
    }

    public void setDatetimePrecision(BigInteger datetimePrecision) {
        this.datetimePrecision = datetimePrecision;
    }

    public String getCharacterSetName() {
        return characterSetName;
    }

    public void setCharacterSetName(String characterSetName) {
        this.characterSetName = characterSetName;
    }

    public String getCollationName() {
        return collationName;
    }

    public void setCollationName(String collationName) {
        this.collationName = collationName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnKey() {
        return columnKey;
    }

    public void setColumnKey(String columnKey) {
        this.columnKey = columnKey;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getPrivileges() {
        return privileges;
    }

    public void setPrivileges(String privileges) {
        this.privileges = privileges;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }
}
