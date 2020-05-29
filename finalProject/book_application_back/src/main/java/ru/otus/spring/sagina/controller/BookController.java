package ru.otus.spring.sagina.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.otus.spring.sagina.dto.mapper.BookDtoMapper;
import ru.otus.spring.sagina.dto.request.CreateBookDto;
import ru.otus.spring.sagina.dto.request.UpdateBookDto;
import ru.otus.spring.sagina.dto.response.BookDto;
import ru.otus.spring.sagina.dto.response.FileResultDto;
import ru.otus.spring.sagina.services.BookService;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.zip.ZipInputStream;

@Api(tags = "BookController")
@RestController
public class BookController {
    private final static Logger LOGGER = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;
    private final BookDtoMapper bookDtoMapper;

    public BookController(BookService bookService,
                          BookDtoMapper bookDtoMapper) {
        this.bookService = bookService;
        this.bookDtoMapper = bookDtoMapper;
    }

    @PostMapping(value = "/book")
    @ApiOperation("создать новую книгу")
    public BookDto createBook(@RequestPart("book") @Valid CreateBookDto book,
                              @RequestPart("file") MultipartFile multipartFile) throws Exception {
        return bookDtoMapper.toDto(bookService.createBook(book, multipartFile));
    }

    @PatchMapping("/book")
    @ApiOperation("измененить книгу")
    public BookDto updateBook(@RequestBody @Valid UpdateBookDto book) {
        return bookDtoMapper.toDto(bookService.updateBook(book));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/book/{id}")
    @ApiOperation("удалить книгу")
    public void deleteBook(@PathVariable("id") UUID bookId) {
        bookService.deleteBook(bookId);
    }

    @GetMapping("/book/{id}")
    @ApiOperation("получить книгу по id")
    public BookDto getBookById(@PathVariable("id") UUID id) {
        return bookDtoMapper.toDto(bookService.getBook(id));
    }

    @GetMapping("/book")
    @ApiOperation("получить список книг")
    public List<BookDto> getListBook(@RequestParam(value = "query", defaultValue = "") String query,
                                     @RequestParam(value = "authorId", required = false) UUID authorId) {
        List<BookDto> result = bookService.getAllBooks(authorId, query).stream()
                .map(bookDtoMapper::toDto)
                .collect(Collectors.toList());
        LOGGER.debug("result books [{}]", result.stream().map(BookDto::getName).collect(Collectors.toList()));
        return result;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "/book/download/{fileId}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ApiOperation("скачать книгу")
    public void downloadFile(@PathVariable("fileId") UUID id, HttpServletResponse response) throws IOException {
        LOGGER.debug("downloading file with id: [{}]", id);
        FileResultDto file = bookService.getFile(id);
        response.addHeader(HttpHeaders.CONTENT_TYPE, file.getContentType());
        response.addHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getFileName());
        IOUtils.copy(file.getContent(), response.getOutputStream());
        response.flushBuffer();
    }

    @GetMapping(value = "/book/read/{fileId}")
    public void showPDF(@PathVariable("fileId") UUID id, HttpServletResponse response) throws IOException {
        LOGGER.debug("reading file with id: [{}]", id);
        FileResultDto file = bookService.getFile(id);
        response.setContentType("application/pdf");
        ZipInputStream zin = new ZipInputStream(file.getContent());
        zin.getNextEntry();
        IOUtils.copy(zin, response.getOutputStream());
        response.flushBuffer();
    }

    @PostMapping(value = "/book/{id}/send")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void sendByEmail(@PathVariable("id") UUID bookId) {
        bookService.sendByEmail(bookId);
    }
}
