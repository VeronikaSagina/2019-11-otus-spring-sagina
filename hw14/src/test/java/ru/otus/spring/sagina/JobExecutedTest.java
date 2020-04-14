package ru.otus.spring.sagina;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.otus.spring.sagina.model.Author;
import ru.otus.spring.sagina.model.Book;
import ru.otus.spring.sagina.model.BookComment;
import ru.otus.spring.sagina.model.Genre;
import ru.otus.spring.sagina.testdata.BookCommentData;
import ru.otus.spring.sagina.testdata.BookData;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@SpringBatchTest
public class JobExecutedTest {
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final RowMapper<Book> BOOK_ROW_MAPPER =
            (rs, rowNum) -> new Book(rs.getString("book_id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    new Author(rs.getString("author_id"), rs.getString("author_name")),
                    List.of());
    public static final RowMapper<Genre> GENRE_ROW_MAPPER =
            (rs, rowNum) -> new Genre(rs.getString("genre_id"), rs.getString("name"));
    public static final RowMapper<BookComment> COMMENT_ROW_MAPPER =
            (rs, rowNum) -> {
                BookComment bookComment = new BookComment();
                bookComment.setId(rs.getString("comment_id"));
                bookComment.setMessage(rs.getString("message"));
                Book book = new Book();
                book.setId(rs.getString("book_id"));
                bookComment.setBook(book);
                return bookComment;
            };


    @BeforeEach
    void clearMetaData() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void jobExecutedTest() throws Exception {
        assertNotNull(jobLauncherTestUtils.getJob());
        jobLauncherTestUtils.launchJob();

        Integer countAuthors = jdbcTemplate.queryForObject("select count(*) from author", Integer.class);
        assertEquals(5, countAuthors);
        Integer countBooks = jdbcTemplate.queryForObject("select count(*) from book", Integer.class);
        assertEquals(4, countBooks);
        Integer countGenres = jdbcTemplate.queryForObject("select count(*) from genre", Integer.class);
        assertEquals(4, countGenres);
        Integer countComments = jdbcTemplate.queryForObject("select count(*) from book_comment", Integer.class);
        assertEquals(5, countComments);
        Integer countBookGenres = jdbcTemplate.queryForObject("select count(*) from book_genre", Integer.class);
        assertEquals(7, countBookGenres);
    }

    @Test
    void stepExecutionTest() {
        assertEquals(BatchStatus.COMPLETED, jobLauncherTestUtils.launchStep("authorLoadStep").getStatus());
        assertEquals(BatchStatus.COMPLETED, jobLauncherTestUtils.launchStep("genreLoadStep").getStatus());
        assertEquals(BatchStatus.COMPLETED, jobLauncherTestUtils.launchStep("bookLoadStep").getStatus());
        assertEquals(BatchStatus.COMPLETED, jobLauncherTestUtils.launchStep("bookGenreLoadStep").getStatus());
        assertEquals(BatchStatus.COMPLETED, jobLauncherTestUtils.launchStep("commentLoadStep").getStatus());
    }

    @Test
    void bookIsRecorded() throws Exception {
        jobLauncherTestUtils.launchJob();
        IntStream.range(1, 4).forEach(id -> {
            Object[] params = {id};
            Book book = jdbcTemplate.queryForObject(
                    "select book.*, author.name as author_name from book " +
                            "join author on book.author_id = author.author_id where book_id = ?", params,
                    BOOK_ROW_MAPPER);
            List<Genre> genres = jdbcTemplate.query(
                    "select book_genre.*, genre.name as name"
                            + " from book_genre join genre"
                            + " on book_genre.genre_id = genre.genre_id where book_id = ?"
                            + " order by book_genre.genre_id", params, GENRE_ROW_MAPPER);
            List<BookComment> comments = jdbcTemplate.query(
                    "select * from book_comment where book_id = ? order by comment_id", params, COMMENT_ROW_MAPPER);
            assertNotNull(book);
            book.setGenres(genres);
            assertEquals(BookData.getBookById(id), book);
            assertEquals(BookCommentData.getCommentsByBookId(id), comments);
        });
    }
}
