package ru.otus.spring.sagina.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.spring.sagina.entity.BookComment;

public interface BookCommentRepository extends ReactiveMongoRepository<BookComment, String> {
    Mono<Void> deleteAllByBookId(String bookId);

    Flux<BookComment> findAllByBookId(String bookId);
}
