package com.example;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.query.QueryDslJdbcTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataConfig {

    @Autowired
    protected DataSource dataSource;

    @Bean
    public JdbcTemplate jdbcTempate() {
        JdbcTemplate template = new JdbcTemplate();
        template.setDataSource(this.dataSource);
        return template;
    }

    @Bean
    public QueryDslJdbcTemplate queryDslJdbcTemplate() {
        QueryDslJdbcTemplate template = new QueryDslJdbcTemplate(
                this.dataSource);
        return template;
    }
}
