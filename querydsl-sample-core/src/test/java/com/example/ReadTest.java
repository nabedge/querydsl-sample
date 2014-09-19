package com.example;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.query.QueryDslJdbcTemplate;
import org.springframework.data.jdbc.query.SqlInsertCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.db.querydsl.gen.Book;
import com.example.db.querydsl.gen.QBook;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLInsertClause;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class, DataConfig.class })
public class ReadTest {

    private final Logger log = LoggerFactory.getLogger(ReadTest.class);

    @Autowired
    protected QueryDslJdbcTemplate queryDslJdbcTemplate;

    @Test
    @Transactional
    public void select1() {
        final QBook qBook = QBook.book;
        SQLQuery query = queryDslJdbcTemplate.newSqlQuery();
        query.from(qBook);
        List<Book> list = queryDslJdbcTemplate.query(query, qBook);
    }

    @Test
    @Transactional
    public void select2() {
        final QBook qBook = QBook.book;
        SQLQuery query = queryDslJdbcTemplate.newSqlQuery();
        query.from(qBook).orderBy(qBook.isbn.asc()).offset(0).limit(3);
        List<Book> list = queryDslJdbcTemplate.query(query, qBook);
    }


}
