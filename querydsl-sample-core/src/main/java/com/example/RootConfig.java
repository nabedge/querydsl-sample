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
    public DataSource realDataSource() {
        logger.debug("creating datasource...");
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        builder.setType(EmbeddedDatabaseType.H2);
        builder.addScript("classpath:sql/00_init.sql");
        EmbeddedDatabase ds = builder.build();
        return ds;
    }

    @Bean
    public DataSource dataSource() {
        return new Log4jdbcProxyDataSource(realDataSource());
    }

}
