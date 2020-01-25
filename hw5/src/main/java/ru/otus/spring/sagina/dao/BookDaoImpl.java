package ru.otus.spring.sagina.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.domain.Author;
import ru.otus.spring.sagina.domain.Book;
import ru.otus.spring.sagina.exceptions.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class BookDaoImpl implements BookDao {
    private static final RowMapper<Book> BOOK_ROW_MAPPER =
            (rs, rowNum) -> new Book(rs.getInt("book_id"), rs.getString("title"),
                    new Author(rs.getInt("author_id"), rs.getString("name")));
    private final NamedParameterJdbcTemplate template;

    public BookDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Book create(Book book) {
        int bookId = getIdFromSequence();
        template.update("insert into book values (:book_id, :title, :author_id)",
                Map.of("book_id", bookId, "title", book.getTitle(), "author_id", book.getAuthor().getId()));
        book.getGenres().forEach(g -> template.update(
                "insert into book_genre values (:book_id, :genre_id)", Map.of(
                        "book_id", bookId, "genre_id", g.getId())));
        return new Book(bookId, book.getTitle(), book.getAuthor(), book.getGenres());
    }

    @Override
    public void update(Book book) {
        if (book.getTitle() != null && book.getAuthor() != null) {
            template.update("update book set title = :title, author_id = :author_id where book_id = :book_id",
                    Map.of("book_id", book.getId(), "title", book.getTitle(), "author_id", book.getAuthor().getId()));
        } else if (book.getTitle() != null && book.getAuthor() == null) {
            template.update("update book set title = :title where book_id = :book_id",
                    Map.of("book_id", book.getId(), "title", book.getTitle()));
        } else if (book.getTitle() == null && book.getAuthor() != null) {
            template.update("update book set author_id = :author_id where book_id = :book_id",
                    Map.of("book_id", book.getId(), "author_id", book.getAuthor().getId()));
        }
        if (book.getGenres().isEmpty()) {
            return;
        }
        template.update("delete from book_genre where book_id = :book_id", Map.of("book_id", book.getId()));
        book.getGenres().forEach(g -> template.update(
                "insert into book_genre values (:book_id, :genre_id)", Map.of(
                        "book_id", book.getId(), "genre_id", g.getId())));
    }

    @Override
    public void delete(List<Integer> bookIds) {
        Map<String, List<Integer>> source = Map.of("book_id", bookIds);
        template.update("delete from book_genre where book_id in (:book_id)", source);
        template.update("delete from book where book_id in (:book_id)", source);
    }

    @Override
    public Book getById(int id) {
        try {
            return template.queryForObject("select book_id, title, book.author_id as author_id, name from book " +
                            "join author on book.author_id = author.author_id where book_id = :book_id order by author_id",
                    Map.of("book_id", id), BOOK_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("не найдена книга с id=%s", id));
        }
    }

    @Override
    public boolean existsById(int id) {
        return template.queryForObject("select count(*) from book where book_id = :book_id",
                Map.of("book_id", id), Integer.class) != 0;
    }

    @Override
    public boolean existsByAuthorId(int authorId) {
        return template.queryForObject("select count(*) from book where author_id = :author_id",
                Map.of("author_id", authorId), Integer.class) != 0;
    }

    @Override
    public Book getByTitle(String title) {
        try {
            return template.queryForObject("select book_id, title, book.author_id as author_id, name from book " +
                            "join author on book.author_id = author.author_id " +
                            "where lower(title) like '%' || :title || '%' order by author_id",
                    Map.of("title", title.toLowerCase()), BOOK_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("не найдена книга %s", title));
        }
    }

    @Override
    public List<Book> getAll() {
        return template.query("select book_id, title, book.author_id as author_id, name from book " +
                "join author on book.author_id = author.author_id  order by author_id", BOOK_ROW_MAPPER);
    }

    @Override
    public List<Book> getAllByAuthor(Author author) {
        return template.query("select * from book where author_id = :author_id order by title",
                Map.of("author_id", author.getId()),
                (rs, rowNum) -> new Book(rs.getInt("book_id"), rs.getString("title"), author));
    }

    @Override
    public List<Integer> getBookGenresIds(int bookId) {
        return template.queryForList(
                "select genre_id from book_genre where book_id = :book_id order by genre_id",
                Map.of("book_id", bookId), Integer.class);
    }

    @Override
    public List<Book> getBooksByGenreId(int genreId) {
        return template.query(
                "select b.book_id, b.title, a.author_id, a.name from book_genre " +
                        " join book b on book_genre.book_id = b.book_id " +
                        " join author a on b.author_id = a.author_id " +
                        " where genre_id = :genre_id",
                Map.of("genre_id", genreId), BOOK_ROW_MAPPER);
    }

    @Override
    public List<Integer> getBooksIdByAuthorId(int authorId) {
        return template.queryForList("select book_id from book where author_id = :author_id",
                Map.of("author_id", authorId), Integer.class);
    }

    private int getIdFromSequence() {
        return template.queryForObject("select seq_book.nextval", Collections.emptyMap(), Integer.class);
    }
}
