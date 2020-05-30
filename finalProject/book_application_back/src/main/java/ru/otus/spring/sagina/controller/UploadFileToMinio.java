package ru.otus.spring.sagina.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.spring.sagina.entity.Book;
import ru.otus.spring.sagina.services.BookService;
import ru.otus.spring.sagina.services.MinioService;

import java.util.UUID;

@Api(tags = "UploadFileToMinio")
@RestController
public class UploadFileToMinio {
    private final MinioService minioService;
    private final BookService bookService;

    public UploadFileToMinio(MinioService minioService, BookService bookService) {
        this.minioService = minioService;
        this.bookService = bookService;
    }

    @PostMapping("/minio/upload")
    @ApiOperation("загрузить книгу")
    private void upload(@RequestParam("id") UUID id, @RequestPart("file") MultipartFile multipartFile) {
        Book book = bookService.getBook(id);
        minioService.uploadBook(book, multipartFile);
    }
}
