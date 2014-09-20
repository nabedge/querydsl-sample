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
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.db.querydsl.gen.Book;
import com.example.db.querydsl.gen.QBook;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.Tuple;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class, DataConfig.class })
public class ReadTest {

    private final Logger log = LoggerFactory.getLogger(ReadTest.class);

    @Autowired
    protected QueryDslJdbcTemplate queryDslJdbcTemplate;

    // =======================================================
    @Test
    @Transactional
    public void select1() {
        final QBook qBook = QBook.book;
        SQLQuery query = queryDslJdbcTemplate.newSqlQuery();
        query.from(qBook);
        List<Book> list = queryDslJdbcTemplate.query(query, qBook);
    }

    // =======================================================
    @Test
    @Transactional
    public void select2() {
        final QBook qBook = QBook.book;
        SQLQuery query = queryDslJdbcTemplate.newSqlQuery();
        query
            .from(qBook)
            .orderBy(qBook.isbn.asc())
            .offset(0)
            .limit(3);
        List<Book> list = queryDslJdbcTemplate.query(query, qBook);
    }

    // =======================================================
    @Test
    @Transactional
    public void select3() {
        final QBook qBook = QBook.book;
        SQLQuery query = queryDslJdbcTemplate.newSqlQuery();
        query.from(qBook);
        query.orderBy(qBook.isbn.asc());
        query.offset(0);
        query.limit(3);
        List<Book> list = queryDslJdbcTemplate.query(query, qBook);
    }

    // =======================================================
    @Test
    @Transactional
    public void select4() {
        final QBook qBook = QBook.book;
        SQLQuery query = queryDslJdbcTemplate.newSqlQuery();
        query.from(qBook);
        query.where(qBook.authorId.eq(2));
        List<Book> list = queryDslJdbcTemplate.query(query, qBook);
        log.debug(list.toString());
    }

    // =======================================================
    @Test
    @Transactional
    public void select5() {
        final QBook qBook = QBook.book;
        SQLQuery query = queryDslJdbcTemplate.newSqlQuery();
        java.sql.Date dt = new java.sql.Date(
                new DateTime("2000-12-31").getMillis());

        query.from(qBook);
        query.where(qBook.authorId.eq(2).and(qBook.publishDate.loe(dt)));
        List<Book> list = queryDslJdbcTemplate.query(query, qBook);
        log.debug(list.toString());
    }

    // =======================================================
    @Test
    @Transactional
    public void select6() {
        final QBook qBook = QBook.book;
        SQLQuery query = queryDslJdbcTemplate.newSqlQuery();
        java.sql.Date dt = new java.sql.Date(
                new DateTime("2000-12-31").getMillis());

        query.from(qBook);
        query.where(qBook.authorId.eq(2));
        query.where(qBook.publishDate.loe(dt));
        List<Book> list = queryDslJdbcTemplate.query(query, qBook);
        log.debug(list.toString());
    }

    // =======================================================
    @Test
    @Transactional
    public void select7() {
        final QBook qBook = QBook.book;
        SQLQuery query = queryDslJdbcTemplate.newSqlQuery();
        java.sql.Date dt = new java.sql.Date(
                new DateTime("2000-12-31").getMillis());

        boolean someParam = true;

        query.from(qBook);
        query.where(qBook.authorId.eq(2));
        if (someParam) {
            query.where(qBook.publishDate.loe(dt));
        }
        List<Book> list = queryDslJdbcTemplate.query(query, qBook);
        log.debug(list.toString());
    }

    // =======================================================
    @Test
    @Transactional
    public void select8() {
        final QBook qBook = QBook.book;
        SQLQuery query = queryDslJdbcTemplate.newSqlQuery();
        java.sql.Date dt = new java.sql.Date(
                new DateTime("2000-12-31").getMillis());

        boolean someParam = true;

        BooleanBuilder bb = new BooleanBuilder();
        bb.and(qBook.authorId.eq(2));
        if (someParam) {
            bb.and(qBook.publishDate.loe(dt));
        }

        query.from(qBook).where(bb);
        List<Book> list = queryDslJdbcTemplate.query(query, qBook);
        log.debug(list.toString());
    }

    // =======================================================
    @Test
    @Transactional
    public void select9() {
        final QBook qBook = QBook.book;
        SQLQuery query = queryDslJdbcTemplate.newSqlQuery();
        query.from(qBook);
        List<Book> list = queryDslJdbcTemplate.query(query, new BookProjection(
                qBook));
    }

    public class BookProjection extends MappingProjection<Book> {

        private static final long serialVersionUID = 1L;

        public BookProjection(QBook qBook) {
            super(Book.class, qBook.isbn, qBook.title);
        }

        @Override
        protected Book map(Tuple row) {
            QBook qBook = QBook.book;
            Book book = new Book();
            book.setIsbn(row.get(qBook.isbn));
            book.setTitle(row.get(qBook.title));
            return book;
        }
    }

}
