package ru.otus.spring.sagina.dao;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.domain.Book;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Service
public class BookDaoImpl implements BookDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            entityManager.persist(book);
            return book;
        } else {
            return entityManager.merge(book);
        }
    }

    @Override
    public void delete(Book book) {
        Query deleteComments = entityManager.createQuery("delete from BookComment where book.id = :bookId");
        deleteComments.setParameter("bookId", book.getId());
        deleteComments.executeUpdate();
        entityManager.remove(book);
    }

    @Override
    public Optional<Book> findById(int id) {
        return Optional.ofNullable(entityManager.find(Book.class, id));
    }

    @Override
    public List<Book> getByIds(List<Integer> ids) {
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b join fetch b.author join fetch b.genres where b.id in :ids", Book.class);
        query.setParameter("ids", ids);
        return query.getResultList();
    }

    @Override
    public List<Book> getByAuthorId(int authorId) {
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b join fetch b.author join fetch b.genres where b.author.id = :authorId",
                Book.class);
        query.setParameter("authorId", authorId);
        return query.getResultList();
    }

    @Override
    public List<Book> getByGenreId(int genreId) {
        TypedQuery<Book> query = entityManager.createQuery(
                "select b from Book b join b.genres g where g.id = :genreId", Book.class);
        query.setParameter("genreId", genreId);
        return query.getResultList();
    }

    @Override
    public List<Book> getByTitle(String title) {
        TypedQuery<Book> query = entityManager.createQuery("select b from Book b join fetch b.author join" +
                        "  fetch b.genres where lower(b.title) like concat( '%',:title,'%') order by b.author.id",
                Book.class);
        query.setParameter("title", title.toLowerCase());
        return query.getResultList();
    }

    @Override
    public List<Book> getAll() {
        TypedQuery<Book> query = entityManager.createQuery(
                "select distinct b from Book b join fetch b.author join fetch b.genres order by b.id", Book.class);
        return query.getResultList();
    }
}
