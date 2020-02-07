package ru.otus.spring.sagina.dao;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.domain.Author;
import ru.otus.spring.sagina.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthorDaoImpl implements AuthorDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Author save(Author author) {
        if (author.getId() == null) {
            entityManager.persist(author);
            return author;
        } else {
            return entityManager.merge(author);
        }
    }

    @Override
    public void delete(Author author) {
        Query deleteBookComments = entityManager.createQuery("delete from BookComment where book.id in :bookIds");
        deleteBookComments.setParameter(
                "bookIds", author.getBooks().stream()
                        .map(Book::getId)
                        .collect(Collectors.toList()));
        deleteBookComments.executeUpdate();
        Query deleteComments = entityManager.createQuery("delete from Book where author.id = :authorId");
        deleteComments.setParameter("authorId", author.getId());
        deleteComments.executeUpdate();
        entityManager.remove(author);
    }

    @Override
    public Optional<Author> findById(int id) {
        return Optional.ofNullable(entityManager.find(Author.class, id));
    }

    @Override
    public Optional<Author> findByIdWithBooks(int id) {
        TypedQuery<Author> query = entityManager.createQuery(
                "select a from Author a join fetch a.books where a.id = :id", Author.class);
        query.setParameter("id", id);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public List<Author> getByName(String name) {
        TypedQuery<Author> query = entityManager.createQuery(
                "select a from Author a where lower(a.name) like concat('%',:name,'%')", Author.class);
        query.setParameter("name", name.toLowerCase());
        return query.getResultList();
    }

    @Override
    public List<Author> getAll() {
        return entityManager.createQuery("select a from Author a ", Author.class).getResultList();
    }
}
