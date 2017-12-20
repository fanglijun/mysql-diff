package com.example.mysqldiff.vo;

import java.io.Serializable;

public class ColumnDiffVo implements Serializable {
    private String columnName;
    private DiffTypeEnum diffType;
    private String col1Type;
    private String col2Type;

    public ColumnDiffVo() {}

    public ColumnDiffVo(String columnName, DiffTypeEnum diffType, String col1Type, String col2Type) {
        this.columnName = columnName;
        this.diffType = diffType;
        this.col1Type = col1Type;
        this.col2Type = col2Type;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public DiffTypeEnum getDiffType() {
        return diffType;
    }

    public void setDiffType(DiffTypeEnum diffType) {
        this.diffType = diffType;
    }

    public String getCol1Type() {
        return col1Type;
    }

    public void setCol1Type(String col1Type) {
        this.col1Type = col1Type;
    }

    public String getCol2Type() {
        return col2Type;
    }

    public void setCol2Type(String col2Type) {
        this.col2Type = col2Type;
    }
}
