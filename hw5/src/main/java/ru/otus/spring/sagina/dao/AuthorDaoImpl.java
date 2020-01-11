package ru.otus.spring.sagina.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.spring.sagina.domain.Author;
import ru.otus.spring.sagina.exceptions.NotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class AuthorDaoImpl implements AuthorDao {
    public static final RowMapper<Author> AUTHOR_ROW_MAPPER =
            (rs, rowNum) -> new Author(rs.getInt("author_id"), rs.getString("name"));
    private final NamedParameterJdbcTemplate template;

    public AuthorDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void create(Author author) {
        template.update("insert into author values (:author_id, :name)",
                Map.of("name", author.getName(), "author_id", author.getId()));
    }

    @Override
    public int update(Author author) {
        return template.update("update author set name = :name where author_id = :author_id",
                Map.of("name", author.getName(), "author_id", author.getId()));
    }

    @Override
    public void delete(int authorId) {
        template.update("delete from author where author_id = :author_id", Map.of("author_id", authorId));
    }

    @Override
    public Author getById(int id) {
        try {
            return template.queryForObject("select * from author where author_id = :author_id",
                    Map.of("author_id", id), AUTHOR_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("не найден автор с id=%s", id));
        }
    }

    @Override
    public List<Author> getByName(String name) {
        return template.query("select * from author where lower(name) like '%' || :name || '%'",
                Map.of("name", name.toLowerCase()), AUTHOR_ROW_MAPPER);
    }

    @Override
    public List<Author> getAll() {
        return template.query("select * from author order by author_id", AUTHOR_ROW_MAPPER);
    }

    @Override
    public int getIdFromSequence() {
        return template.queryForObject("select seq_author.nextval", Collections.emptyMap(), Integer.class);
    }

    @Override
    public boolean existsById(Integer id) {
        return template.queryForObject("select count(*) from author where author_id = :author_id",
                Map.of("author_id", id), Integer.class) != 0;
    }
}
