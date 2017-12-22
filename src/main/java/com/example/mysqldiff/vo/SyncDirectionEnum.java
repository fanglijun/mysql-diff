package com.example.mysqldiff.vo;

/**
 * 同步方向
 */
public enum SyncDirectionEnum {
    DB1_TO_DB2, // 修改DB2，使它与DB1一致
    DB2_TO_DB1; // 修改DB1，使它与DB2一致
}
