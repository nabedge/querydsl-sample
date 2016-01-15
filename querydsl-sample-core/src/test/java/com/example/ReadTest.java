package com.example;

import com.example.db.querydsl.gen.Book;
import com.example.db.querydsl.gen.QAuthor;
import com.example.db.querydsl.gen.QBook;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.sql.SQLBindings;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLQueryFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { Config.class })
public class ReadTest {

    private final Logger log = LoggerFactory.getLogger(ReadTest.class);

    @Autowired
    protected SQLQueryFactory sqlQueryFactory;

    // =======================================================
    @Test
    @Transactional
    public void select21() {
        final QBook qBook = QBook.book;
        SQLQuery<Book> query = sqlQueryFactory.select(qBook);
        query.from(qBook);
        // The case that you need special dialect.
        // (supposing that "BETWEEN" is a dialect.)
        String template = "publish_date between {0} and {1}";
        query.where(Expressions.booleanTemplate(template, "1980-01-01", "2000-12-31"));
        List<Book> list = query.fetch();
    }

    // =======================================================
    @Test
    @Transactional
    public void select22() {
        final QBook qBook = QBook.book;
        final QAuthor qAuthor = QAuthor.author;
        SQLQuery<BookAndAuthorDTO> query = sqlQueryFactory.select(
                Projections.bean(BookAndAuthorDTO.class,
                        qBook.isbn,
                        qBook.title,
                        qAuthor.name.as("authorName"),
                        qBook.publishDate));
        query.from(qBook, qAuthor);
        query.where(qBook.authorId.eq(qAuthor.id));
        query.orderBy(qBook.publishDate.asc(), qAuthor.id.asc());

        SQLBindings sqlBindings = query.getSQL();
        log.debug(sqlBindings.getSQL());

        List<BookAndAuthorDTO> list = query.fetch();
        list.stream().forEach(e -> {
            log.debug("--------");
            log.debug("isbn:{}", e.getIsbn());
            log.debug("title:{}", e.getTitle());
            log.debug("author:{}", e.getAuthorName());
            log.debug("publishDate:{}", e.getPublishDate());
        });
    }

    // =======================================================
    @Test
    @Transactional
    public void select23() {
        final QBook qBook = QBook.book;
        final QAuthor qAuthor = QAuthor.author;
        SQLQuery<BookAndAuthorDTO> query = sqlQueryFactory.select(
                Projections.bean(BookAndAuthorDTO.class,
                        qBook.isbn,
                        qBook.title,
                        qAuthor.name.as("authorName"),
                        qBook.publishDate));
        query.from(qBook);
        query.innerJoin(qAuthor).on(qAuthor.id.eq(qBook.authorId));
        query.where(qBook.authorId.eq(qAuthor.id));
        query.orderBy(qBook.publishDate.asc(), qAuthor.id.asc());

        SQLBindings sqlBindings = query.getSQL();
        log.debug(sqlBindings.getSQL());

        List<BookAndAuthorDTO> list = query.fetch();
        list.stream().forEach(e -> {
            log.debug("--------");
            log.debug("isbn:{}", e.getIsbn());
            log.debug("title:{}", e.getTitle());
            log.debug("author:{}", e.getAuthorName());
            log.debug("publishDate:{}", e.getPublishDate());
        });
    }

}
