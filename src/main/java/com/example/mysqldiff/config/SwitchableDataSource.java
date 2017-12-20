package com.example.mysqldiff.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 可手动切换的数据源
 */
public class SwitchableDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> activeDatasource = new ThreadLocal<>();

    @Override
    protected Object determineCurrentLookupKey() {
        return activeDatasource.get();
    }

    public static void switchDatasource(String datasource) {
        activeDatasource.set(datasource);
    }
}
