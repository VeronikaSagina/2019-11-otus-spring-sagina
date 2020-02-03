package ru.otus.spring.sagina.shell;

import org.springframework.shell.standard.ShellCommandGroup;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import ru.otus.spring.sagina.dto.request.CreateGenreDto;
import ru.otus.spring.sagina.dto.request.UpdateGenreDto;
import ru.otus.spring.sagina.dto.response.GenreDto;
import ru.otus.spring.sagina.services.GenreService;

import javax.validation.Valid;
import java.util.List;

@ShellComponent
@ShellCommandGroup("Genre commands")
public class GenreEventsCommands {
    private final GenreService genreService;

    public GenreEventsCommands(GenreService genreService) {
        this.genreService = genreService;
    }

    @ShellMethod(value = "create new genre", key = {"cg", "createGenre"})
    public GenreDto createGenre(@ShellOption(help = "cg '{\"type\":\"поэма\"}'") @Valid CreateGenreDto genreDto) {
        return genreService.createGenre(genreDto);
    }

    @ShellMethod(value = "update genre", key = {"ug", "updateGenre"})
    public GenreDto updateGenre(@ShellOption(help = "ug '{\"id\":3,\"type\":\"сказка\"}'")
                                            @Valid UpdateGenreDto genreDto) {
        return genreService.updateGenre(genreDto);
    }

    @ShellMethod(value = "getting list of genres", key = {"allg", "getAllGenres"})
    public List<GenreDto> getAll() {
        return genreService.getAll();
    }

    @ShellMethod(value = "getting list of genres by genre type", key = {"g", "getGenresByType"})
    public List<GenreDto> getByType(String type) {
        return genreService.getByType(type);
    }
}
