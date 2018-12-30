package com.yinhai.tty.util;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {
    @Primary
    @Bean(name = "oracledatasource")
    @ConfigurationProperties("spring.datasource.druid.oracle")
    public DataSource dataSourceTwo(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "oracleJdbcTemplate")
    public JdbcTemplate secondaryJdbcTemplate(
            @Qualifier("oracledatasource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}