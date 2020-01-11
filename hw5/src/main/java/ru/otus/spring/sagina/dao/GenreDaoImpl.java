package ru.otus.spring.sagina.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.spring.sagina.domain.Genre;
import ru.otus.spring.sagina.exceptions.NotFoundException;

import java.util.*;

@Repository
public class GenreDaoImpl implements GenreDao {
    public static final RowMapper<Genre> GENRE_ROW_MAPPER =
            (rs, rowNum) -> new Genre(rs.getInt("genre_id"), rs.getString("type"));
    private final NamedParameterJdbcTemplate template;

    public GenreDaoImpl(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public void create(Genre genre) {
        template.update("insert into genre values (:genre_id, :type)",
                Map.of("genre_id", genre.getId(), "type", genre.getType()));
    }

    @Override
    public int update(Genre genre) {
        return template.update("update genre set type = :type where genre_id = :genre_id",
                Map.of("genre_id", genre.getId(), "type", genre.getType()));
    }

    @Override
    public Genre getById(int id) {
        try {
            return template.queryForObject(
                    "select * from genre where genre_id = :genre_id", Map.of("genre_id", id), GENRE_ROW_MAPPER);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException(String.format("не найден жанр с id=%s", id));
        }
    }

    @Override
    public Set<Genre> getByIds(List<Integer> ids) {
        return new HashSet<>(template.query("select * from genre where genre_id in (:ids)",
                Map.of("ids", ids), GENRE_ROW_MAPPER));
    }

    @Override
    public List<Genre> getByType(String type) {
        return template.query(
                "select * from genre where lower(type) like '%' || :type || '%'",
                Map.of("type", type.toLowerCase()), GENRE_ROW_MAPPER);
    }

    @Override
    public List<Genre> getAll() {
        return template.query("select * from genre order by type", GENRE_ROW_MAPPER);
    }

    @Override
    public int getIdFromSequence() {
        return template.queryForObject("select seq_genre.nextval", Collections.emptyMap(), Integer.class);
    }

    @Override
    public boolean existsById(int id) {
        return template.queryForObject("select count(*) from genre where genre_id = :genre_id",
                Map.of("genre_id", id), Integer.class) != 0;
    }
}
