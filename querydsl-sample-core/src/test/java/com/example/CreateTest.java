package com.example;

import static org.hamcrest.CoreMatchers.is;

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
import com.mysema.query.sql.dml.SQLInsertClause;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class, DataConfig.class })
public class CreateTest {

    private final Logger log = LoggerFactory.getLogger(CreateTest.class);

    @Autowired
    protected QueryDslJdbcTemplate queryDslJdbcTemplate;

    // =======================================================
    @Test
    @Transactional()
    public void insert1() {

        Book book = new Book();
        book.setIsbn("999-9999999999");
        book.setAuthorId(4);
        book.setTitle("進撃の巨人 （１）");
        java.sql.Date publishDate = new java.sql.Date(
                new DateTime("2010-03-17").getMillis());
        book.setPublishDate(publishDate);

        final QBook qBook = QBook.book;

        long result = queryDslJdbcTemplate.insert(qBook,
                new SqlInsertCallback() {
                    @Override
                    public long doInSqlInsertClause(SQLInsertClause insert) {
                        return insert.populate(book).execute();
                    }
                });
    }

    // =======================================================
    @Test
    @Transactional
    public void insert2() {

        Book book = new Book();
        book.setIsbn("999-9999999999");
        book.setAuthorId(4);
        book.setTitle("進撃の巨人 （１）");
        book.setPublishDate(new java.sql.Date(new DateTime("2010-03-17")
                .getMillis()));

        final QBook qBook = QBook.book;

        long result = queryDslJdbcTemplate.insert(qBook, //
                insert -> insert.populate(book).execute());
    }

    // =======================================================
    @Test
    @Transactional
    public void insert3() {

        java.sql.Date publishDate = new java.sql.Date(
                new DateTime("200-03-17").getMillis());

        final QBook qBook = QBook.book;

        long result = queryDslJdbcTemplate.insert(qBook,
                new SqlInsertCallback() {
                    @Override
                    public long doInSqlInsertClause(SQLInsertClause insert) {
                        insert.set(qBook.isbn, "999-9999999999");
                        insert.set(qBook.title, "進撃の巨人 （１）");
                        insert.set(qBook.authorId, 4);
                        insert.set(qBook.publishDate, publishDate);
                        return insert.execute();
                    }
                });
    }

    // =======================================================
    @Test
    @Transactional
    public void insert4() {

        java.sql.Date publishDate = new java.sql.Date(
                new DateTime("200-03-17").getMillis());

        final QBook qBook = QBook.book;

        long result = queryDslJdbcTemplate.insert(qBook, insert -> {
            insert.set(qBook.isbn, "999-9999999999");
            insert.set(qBook.title, "進撃の巨人 （１）");
            insert.set(qBook.authorId, 4);
            insert.set(qBook.publishDate, publishDate);
            return insert.execute();
        });
    }
}
