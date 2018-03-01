package com.example.videorentalstore.film.web;

import com.example.videorentalstore.film.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {


    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAll() {
        return Arrays.asList(
                new NewReleaseFilm("Matrix 11"),
                new RegularReleaseFilm("Spider Man"),
                new RegularReleaseFilm("Spider Man 2"),
                new OldReleaseFilm("Out of Africa")
        );
    }
}
