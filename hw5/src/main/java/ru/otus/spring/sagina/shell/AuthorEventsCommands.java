package ru.otus.spring.sagina.shell;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.sagina.dto.request.CreateAuthorDto;
import ru.otus.spring.sagina.dto.request.UpdateAuthorDto;
import ru.otus.spring.sagina.dto.response.AuthorDto;
import ru.otus.spring.sagina.services.AuthorService;

import javax.validation.Valid;
import java.util.List;

@ShellComponent
@ShellCommandGroup("Author commands")
public class AuthorEventsCommands {
    private final AuthorService authorService;

    public AuthorEventsCommands(AuthorService authorService) {
        this.authorService = authorService;
    }

    @ShellMethod(value = "create new author", key = {"ca", "createAuthor"})
    public AuthorDto createAuthor(@ShellOption(help = "ca '{\"name\":\"Александр Пушкин\"}'")
                                              @Valid CreateAuthorDto author) {
        return authorService.createAuthor(author);
    }

    @ShellMethod(value = "update author", key = {"ua", "updateAuthor"})
    public AuthorDto updateAuthor(@ShellOption(help = "ua '{\"id\":4,\"name\":\"Джордж Мартин\"}'")
                                         @Valid UpdateAuthorDto author) {
        return authorService.updateAuthor(author);
    }

    @ShellMethod(value = "getting author by name", key = {"gan", "getAuthor"})
    public List<AuthorDto> getAuthorByName(@ShellOption String name) {
        return authorService.getAuthorByName(name);
    }

    @ShellMethod(value = "getting author by id", key = {"gai", "getAuthorById"})
    public AuthorDto getAuthorById(@ShellOption int id) {
        return authorService.getAuthorById(id);
    }

    @ShellMethod(value = "getting list of authors", key = {"alla", "getAllAuthors"})
    public List<AuthorDto> getListAuthor() {
        return authorService.getAll();
    }

    @ShellMethod(value = "delete author by id", key = {"da", "deleteAuthor"})
    public void deleteAuthor(@ShellOption int authorId) {
        authorService.deleteAuthor(authorId);
    }

    @ShellMethod(value = "delete author by id with all books", key = {"dawb", "deleteAuthorWithBooks"})
    public void deleteAuthorWithBooks(@ShellOption int authorId) {
        authorService.deleteAuthorWithBooks(authorId);
    }
}
