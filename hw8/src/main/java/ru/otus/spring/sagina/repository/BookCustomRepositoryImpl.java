package ru.otus.spring.sagina.repository;

import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import ru.otus.spring.sagina.entity.Book;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Repository
public class BookCustomRepositoryImpl implements BookCustomRepository {
    private final MongoOperations mongoOperations;

    public BookCustomRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Book findFirstByGenreId(String genreId) {
        return mongoOperations.findOne(Query.query(where("genres.id").is(genreId)), Book.class);
    }

    @Override
    public List<Book> findAllByGenreId(String genreId) {
        return mongoOperations.find(Query.query(where("genres.id").is(genreId)), Book.class);
    }
}
