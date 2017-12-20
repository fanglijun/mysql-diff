package com.example.mysqldiff.config;

import org.apache.ibatis.datasource.pooled.PooledDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "db")
public class DatabaseConfig {
    private String driver;
    public static final String DATASOURCE_1 = "DATASOURCE_1";
    private String ds1Url;
    private String ds1Username;
    private String ds1Password;
    private String ds1Db;

    public static final String DATASOURCE_2 = "DATASOURCE_2";
    private String ds2Url;
    private String ds2Username;
    private String ds2Password;
    private String ds2Db;

    private DataSource createDataSource(String url, String username, String password) {
        PooledDataSource dataSource = new PooledDataSource();
        dataSource.setDriver(this.driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

    @Bean
    public DataSource dataSource() {
        Map<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put(DATASOURCE_1, this.createDataSource(ds1Url, ds1Username, ds1Password));
        dataSourceMap.put(DATASOURCE_2, this.createDataSource(ds2Url, ds2Username, ds2Password));
        SwitchableDataSource switchableDataSource = new SwitchableDataSource();
        switchableDataSource.setTargetDataSources(dataSourceMap);
        return switchableDataSource;
    }

    public static void switchDatasource(String datasource) {
        if (!DATASOURCE_1.equals(datasource) && !DATASOURCE_2.equals(datasource)) {
            throw new RuntimeException("Datasource name error");
        }
        SwitchableDataSource.switchDatasource(datasource);
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getDs1Url() {
        return ds1Url;
    }

    public void setDs1Url(String ds1Url) {
        this.ds1Url = ds1Url;
    }

    public String getDs1Username() {
        return ds1Username;
    }

    public void setDs1Username(String ds1Username) {
        this.ds1Username = ds1Username;
    }

    public String getDs1Password() {
        return ds1Password;
    }

    public void setDs1Password(String ds1Password) {
        this.ds1Password = ds1Password;
    }

    public String getDs1Db() {
        return ds1Db;
    }

    public void setDs1Db(String ds1Db) {
        this.ds1Db = ds1Db;
    }

    public String getDs2Url() {
        return ds2Url;
    }

    public void setDs2Url(String ds2Url) {
        this.ds2Url = ds2Url;
    }

    public String getDs2Username() {
        return ds2Username;
    }

    public void setDs2Username(String ds2Username) {
        this.ds2Username = ds2Username;
    }

    public String getDs2Password() {
        return ds2Password;
    }

    public void setDs2Password(String ds2Password) {
        this.ds2Password = ds2Password;
    }

    public String getDs2Db() {
        return ds2Db;
    }

    public void setDs2Db(String ds2Db) {
        this.ds2Db = ds2Db;
    }
}
