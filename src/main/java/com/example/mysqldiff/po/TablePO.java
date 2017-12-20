package com.example.mysqldiff.po;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

/**
 * 对应表 TABLES
 */
public class TablePO implements Serializable {
    private String tableCatalog;
    private String tableSchema;
    private String tableName;
    private String tableType;
    private String engine;
    private BigInteger version;
    private String rowFormat;
    private BigInteger tableRows;
    private BigInteger avgRowLength;
    private BigInteger dataLength;
    private BigInteger maxDataLength;
    private BigInteger indexLength;
    private BigInteger dataFree;
    private BigInteger autoIncrement;
    private Date createTime;
    private Date updateTime;
    private Date checkTime;
    private String tableCollation;
    private BigInteger checksum;
    private String createOptions;
    private String tableComment;

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

    public String getTableType() {
        return tableType;
    }

    public void setTableType(String tableType) {
        this.tableType = tableType;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public BigInteger getVersion() {
        return version;
    }

    public void setVersion(BigInteger version) {
        this.version = version;
    }

    public String getRowFormat() {
        return rowFormat;
    }

    public void setRowFormat(String rowFormat) {
        this.rowFormat = rowFormat;
    }

    public BigInteger getTableRows() {
        return tableRows;
    }

    public void setTableRows(BigInteger tableRows) {
        this.tableRows = tableRows;
    }

    public BigInteger getAvgRowLength() {
        return avgRowLength;
    }

    public void setAvgRowLength(BigInteger avgRowLength) {
        this.avgRowLength = avgRowLength;
    }

    public BigInteger getDataLength() {
        return dataLength;
    }

    public void setDataLength(BigInteger dataLength) {
        this.dataLength = dataLength;
    }

    public BigInteger getMaxDataLength() {
        return maxDataLength;
    }

    public void setMaxDataLength(BigInteger maxDataLength) {
        this.maxDataLength = maxDataLength;
    }

    public BigInteger getIndexLength() {
        return indexLength;
    }

    public void setIndexLength(BigInteger indexLength) {
        this.indexLength = indexLength;
    }

    public BigInteger getDataFree() {
        return dataFree;
    }

    public void setDataFree(BigInteger dataFree) {
        this.dataFree = dataFree;
    }

    public BigInteger getAutoIncrement() {
        return autoIncrement;
    }

    public void setAutoIncrement(BigInteger autoIncrement) {
        this.autoIncrement = autoIncrement;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getTableCollation() {
        return tableCollation;
    }

    public void setTableCollation(String tableCollation) {
        this.tableCollation = tableCollation;
    }

    public BigInteger getChecksum() {
        return checksum;
    }

    public void setChecksum(BigInteger checksum) {
        this.checksum = checksum;
    }

    public String getCreateOptions() {
        return createOptions;
    }

    public void setCreateOptions(String createOptions) {
        this.createOptions = createOptions;
    }

    public String getTableComment() {
        return tableComment;
    }

    public void setTableComment(String tableComment) {
        this.tableComment = tableComment;
    }
}
