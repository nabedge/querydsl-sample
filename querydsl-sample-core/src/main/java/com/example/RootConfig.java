package com.example;

import javax.sql.DataSource;

import net.sf.log4jdbc.Log4jdbcProxyDataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@EnableAutoConfiguration
@Configuration
class RootConfig {

    private final static Logger logger = LoggerFactory
            .getLogger(RootConfig.class);

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.setType(EmbeddedDatabaseType.H2);
        builder.addScript("classpath:sql/00_init.sql");
        EmbeddedDatabase ds = builder.build();
        Log4jdbcProxyDataSource proxyDs = new Log4jdbcProxyDataSource(ds);
        return proxyDs;
    }

}
