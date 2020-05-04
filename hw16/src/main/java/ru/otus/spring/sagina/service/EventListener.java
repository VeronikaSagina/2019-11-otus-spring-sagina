package ru.otus.spring.sagina.service;

import lombok.extern.java.Log;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Service;
import ru.otus.spring.sagina.entity.Author;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.repository.BookRepository;

import java.util.List;

@Log
@Service
@RepositoryEventHandler
public class EventListener {
    private final BookRepository bookRepository;

    public EventListener(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @HandleBeforeDelete
    public void handleAuthorBeforeDelete(Author author) {
        log.info("Удаление книг перед удалением автора");
        List<Book> books = bookRepository.findAllByAuthorId(author.getId());
        if (!books.isEmpty()){
            bookRepository.deleteAll(books);
        }
    }
}
