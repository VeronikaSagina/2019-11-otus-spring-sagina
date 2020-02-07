package ru.otus.spring.sagina.dao;

import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.domain.BookComment;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Service
public class BookCommentDaoImpl implements BookCommentDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BookComment save(BookComment comment) {
        if (comment.getId() == null) {
            entityManager.persist(comment);
            return comment;
        } else {
            return entityManager.merge(comment);
        }
    }

    @Override
    public Optional<BookComment> findById(int id) {
        return Optional.ofNullable(entityManager.find(BookComment.class, id));
    }

    @Override
    public List<BookComment> getAllByBookId(int bookId) {
        TypedQuery<BookComment> query = entityManager.createQuery(
                "select c from BookComment c join c.book where c.book.id = :bookId order by c.id", BookComment.class);
        query.setParameter("bookId", bookId);
        return query.getResultList();
    }

    @Override
    public void delete(BookComment bookComment) {
        entityManager.remove(bookComment);
    }

    @Override
    public void deleteAllByBookId(int bookId) {
        Query query = entityManager.createQuery("delete from BookComment b where b.book.id = :bookId");
        query.setParameter("bookId", bookId);
        query.executeUpdate();
    }

    @Override
    public void deleteById(int id) {
        Query query = entityManager.createQuery("delete from BookComment b where b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
