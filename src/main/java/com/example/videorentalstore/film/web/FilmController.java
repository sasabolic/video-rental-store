package com.example.videorentalstore.film.web;

import com.example.videorentalstore.film.Film;
import com.example.videorentalstore.film.FilmService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/films")
public class FilmController {


    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Iterable<Film> getAll() {
        return this.filmService.findAll();
    }
}
