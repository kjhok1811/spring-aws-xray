package com.example.awsxray.xray;

import com.amazonaws.xray.sql.TracingDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

@Configuration
public class XRayDataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "aws.xray.datasource")
    public HikariConfig hikariConfig() {
        return new HikariConfig();
    }
    @Bean
    public DataSource tracingDataSource(HikariConfig hikariConfig) {
        return TracingDataSource.decorate(new HikariDataSource(hikariConfig));
    }
}
