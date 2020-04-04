package ru.otus.spring.sagina.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.otus.spring.sagina.services.BookCommentService;

import javax.validation.Valid;

@Controller
public class BookCommentController {
    private final BookCommentService commentService;

    public BookCommentController(BookCommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/{bookId}/comment/create")
    public String createComment(@PathVariable("bookId") String bookId, @ModelAttribute("message") @Valid String message) {
        if (!message.isBlank()) {
            commentService.create(bookId, message);
        }
        return "redirect:/book/{bookId}";
    }
}
