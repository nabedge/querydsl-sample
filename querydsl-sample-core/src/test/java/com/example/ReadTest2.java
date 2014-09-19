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
import com.example.db.querydsl.gen.QAuthor;
import com.example.db.querydsl.gen.QBook;
import com.mysema.query.BooleanBuilder;
import com.mysema.query.Tuple;
import com.mysema.query.sql.SQLQuery;
import com.mysema.query.sql.dml.SQLInsertClause;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.Expression;
import com.mysema.query.types.MappingProjection;
import com.mysema.query.types.template.SimpleTemplate;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { RootConfig.class, DataConfig.class })
public class ReadTest2 {

    private final Logger log = LoggerFactory.getLogger(ReadTest2.class);

    @Autowired
    protected QueryDslJdbcTemplate queryDslJdbcTemplate;

    // =======================================================
    @Test
    @Transactional
    public void select21() {
        final QBook qBook = QBook.book;
        SQLQuery query = queryDslJdbcTemplate.newSqlQuery();
        query.from(qBook);
        // 何か特殊なdialect（方言）を使いたい場合。ここでは仮にbetweenが方言だとして
        // The case that you need special dialect. (supposing that "BETWEEN" is a dialect.)
        String template = "publish_date between {0} and {1}";
        query.where(Expressions.booleanTemplate(template, "1980-01-01",
                "2000-12-31"));
        List<Book> list = queryDslJdbcTemplate.query(query, qBook);
    }

    // =======================================================
    @Test
    @Transactional
    public void select22() {
        final QBook qBook = QBook.book;
        final QAuthor qAuthor = QAuthor.author;
        SQLQuery query = queryDslJdbcTemplate.newSqlQuery();
        query.from(qBook, qAuthor);
        query.where(qBook.authorId.eq(qAuthor.id));
        query.orderBy(qBook.publishDate.asc(), qAuthor.id.asc());
        List<BookAndAuthorDTO> list = queryDslJdbcTemplate.query(query,
                new BookAndAuthorProjection(qBook, qAuthor));
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
        SQLQuery query = queryDslJdbcTemplate.newSqlQuery();
        query.from(qBook);
        query.innerJoin(qAuthor).on(qAuthor.id.eq(qBook.authorId));
        query.orderBy(qBook.publishDate.asc(), qAuthor.id.asc());
        List<BookAndAuthorDTO> list = queryDslJdbcTemplate.query(query,
                new BookAndAuthorProjection(qBook, qAuthor));
        list.stream().forEach(e -> {
            log.debug("--------");
            log.debug("isbn:{}", e.getIsbn());
            log.debug("title:{}", e.getTitle());
            log.debug("author:{}", e.getAuthorName());
            log.debug("publishDate:{}", e.getPublishDate());
        });
    }

    public class BookAndAuthorProjection extends
            MappingProjection<BookAndAuthorDTO> {

        private static final long serialVersionUID = 1L;

        public BookAndAuthorProjection(QBook qBook, QAuthor qAuthor) {
            super(BookAndAuthorDTO.class, //
                    qBook.isbn, //
                    qBook.title, //
                    qBook.publishDate, //
                    qAuthor.name);
        }

        @Override
        protected BookAndAuthorDTO map(Tuple row) {
            final QBook qBook = QBook.book;
            final QAuthor qAuthor = QAuthor.author;
            BookAndAuthorDTO dto = new BookAndAuthorDTO();
            dto.setIsbn(row.get(qBook.isbn));
            dto.setTitle(row.get(qBook.title));
            dto.setAuthorName(row.get(qAuthor.name));
            dto.setPublishDate(row.get(qBook.publishDate));
            return dto;
        }
    }
}
