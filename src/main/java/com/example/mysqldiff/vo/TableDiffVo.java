package com.example.mysqldiff.vo;

import java.io.Serializable;
import java.util.List;

public class TableDiffVo implements Serializable {
    private String tableName;
    private DiffTypeEnum diffType;
    private List<ColumnDiffVo> columnDiffs;

    public TableDiffVo() {}

    public TableDiffVo(String tableName, DiffTypeEnum diffType, List<ColumnDiffVo> columnDiffs) {
        this.tableName = tableName;
        this.diffType = diffType;
        this.columnDiffs = columnDiffs;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public DiffTypeEnum getDiffType() {
        return diffType;
    }

    public void setDiffType(DiffTypeEnum diffType) {
        this.diffType = diffType;
    }

    public List<ColumnDiffVo> getColumnDiffs() {
        return columnDiffs;
    }

    public void setColumnDiffs(List<ColumnDiffVo> columnDiffs) {
        this.columnDiffs = columnDiffs;
    }
}
