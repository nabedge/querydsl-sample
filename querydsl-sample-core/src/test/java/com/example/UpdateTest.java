package com.example;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.query.QueryDslJdbcTemplate;
import org.springframework.data.jdbc.query.SqlUpdateCallback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.db.querydsl.gen.Book;
import com.example.db.querydsl.gen.QBook;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLUpdateClause;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class, DataConfig.class })
public class UpdateTest {

    private final Logger log = LoggerFactory.getLogger(UpdateTest.class);

    @Autowired
    protected QueryDslJdbcTemplate queryDslJdbcTemplate;

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

    // =======================================================
    @Test
    @Transactional
    public void update2() {
        final QBook qBook = QBook.book;
        queryDslJdbcTemplate.update(qBook, update -> {
            update.set(qBook.title, "xxxxx");
            update.where(qBook.isbn.eq("999-9999999999"));
            return update.execute();
        });
    }
    
    // =======================================================
    @Test
    @Transactional
    public void update3() {
        final QBook qBook = QBook.book;
        String isbn = "001-0000000001";

        // select 
        SQLQuery query = queryDslJdbcTemplate.newSqlQuery();
        query.from(qBook).where(qBook.isbn.eq(isbn));
        Book book = queryDslJdbcTemplate.queryForObject(query, qBook);
        
        // modify
        book.setTitle("xxxxxxxxxx");
        
        // update
        queryDslJdbcTemplate.update(qBook, update -> {
            update.populate(book);
            update.where(qBook.isbn.eq(isbn));
            return update.execute();
        });
    }
    

}
