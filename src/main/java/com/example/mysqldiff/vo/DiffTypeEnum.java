package com.example.mysqldiff.vo;

/**
 * 差异类型
 */
public enum DiffTypeEnum {
    ADDED, // db1中新增
    DELETED, // db1中没有，db2中新增
    CHANGED; // 修改
}
