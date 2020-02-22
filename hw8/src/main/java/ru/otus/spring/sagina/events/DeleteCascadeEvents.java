package ru.otus.spring.sagina.events;

import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.repository.BookCommentRepository;
import ru.otus.spring.sagina.repository.BookRepository;

import java.util.List;
import java.util.Objects;

@Component
public class DeleteCascadeEvents extends AbstractMongoEventListener<Object> {
    private final BookRepository bookRepository;
    private final BookCommentRepository bookCommentRepository;

    public DeleteCascadeEvents(BookRepository bookRepository,
                               BookCommentRepository bookCommentRepository) {
        this.bookRepository = bookRepository;
        this.bookCommentRepository = bookCommentRepository;
    }

    @Override
    public void onBeforeDelete(BeforeDeleteEvent<Object> event) {
        String id = String.valueOf(event.getSource().getString("_id"));
        if (Objects.equals(event.getCollectionName(), "author")) {
            List<Book> books = bookRepository.findAllByAuthorIdIn(List.of(id));
            if (!CollectionUtils.isEmpty(books)) {
                bookRepository.deleteAll(books);
            }
        } else if (Objects.equals(event.getCollectionName(), "genre")) {
            if (bookRepository.findFirstByGenreId(id) != null) {
                throw new UnsupportedOperationException("сначала удалите все книги с этим жаром");
            }
        } else if (Objects.equals(event.getCollectionName(), "book")) {
            bookCommentRepository.deleteAllByBookId(id);
        }
    }
}
