package ru.otus.spring.sagina.dao;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.domain.Genre;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Service
public class GenreDaoImpl implements GenreDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Genre save(Genre genre) {
        if (genre.getId() == null) {
            entityManager.persist(genre);
            return genre;
        } else {
            return entityManager.merge(genre);
        }
    }

    @Override
    public Optional<Genre> findById(int id) {
        return Optional.ofNullable(entityManager.find(Genre.class, id));
    }

    @Override
    public List<Genre> getByIds(List<Integer> ids) {
        TypedQuery<Genre> query = entityManager.createQuery(
                "select g from Genre g where g.id in :ids", Genre.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public List<Genre> getByType(String type) {
        TypedQuery<Genre> query = entityManager.createQuery(
                "select g from Genre g where lower(g.type) like concat('%', :type, '%') ", Genre.class);
        query.setParameter("type", type.toLowerCase());
        return query.getResultList();
    }

    @Override
    public List<Genre> getAll() {
        return entityManager.createQuery("select g from Genre g order by g.type", Genre.class).getResultList();
    }
}
