package com.example;

import static org.hamcrest.CoreMatchers.is;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jdbc.query.QueryDslJdbcTemplate;
import org.springframework.data.jdbc.query.SqlInsertCallback;
import org.springframework.data.jdbc.query.SqlUpdateCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.db.querydsl.gen.Book;
import com.example.db.querydsl.gen.QAuthor;
import com.example.db.querydsl.gen.QBook;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.Tuple;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.sql.dml.SQLUpdateClause;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import com.mysema.query.types.template.SimpleTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class, DataConfig.class })
public class UpdateTest {

    private final Logger log = LoggerFactory.getLogger(UpdateTest.class);

    @Autowired
    protected QueryDslJdbcTemplate queryDslJdbcTemplate;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    // =======================================================
    @Test
    @Transactional
    public void update1() {
        final QBook qBook = QBook.book;
        queryDslJdbcTemplate.update(qBook, new SqlUpdateCallback() {

            @Override
            public long doInSqlUpdateClause(SQLUpdateClause update) {
                update.set(qBook.title, "xxxxx");
                update.where(qBook.isbn.eq("999-9999999999"));
                return update.execute();
            }
        });
    }

    @Test
    // @Transactional
    public void update2() {

        // QueryDSLでは表現が難しい、すごく複雑なsqlのつもり。
        String sql = "update author set name = 'Mr Who' where id = 1";
        jdbcTemplate.execute(sql);

        final QBook qBook = QBook.book;
        queryDslJdbcTemplate.update(qBook, new SqlUpdateCallback() {
            @Override
            public long doInSqlUpdateClause(SQLUpdateClause update) {
                update.set(qBook.title, "xxxxx");
                update.where(qBook.isbn.eq("999-9999999999"));
                return update.execute();
            }
        });
        throw new RuntimeException();
        // jdbcTemplate, queryDslJdbcTemplateの
        // それぞれで発行したupdateの両方がrollbackされるはず。
    }

}
