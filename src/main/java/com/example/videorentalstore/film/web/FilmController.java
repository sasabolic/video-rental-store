package com.example.videorentalstore.film.web;

import com.example.videorentalstore.film.Film;
import com.example.videorentalstore.film.FilmNotFoundException;
import com.example.videorentalstore.film.FilmService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/films")
public class FilmController {


    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Iterable<Film>> getAll(@RequestParam(required = false) String name) {
        if (name == null) {
            return ResponseEntity.ok(this.filmService.findAll());
        }
        return ResponseEntity.ok(this.filmService.findAllByName(name));
    }

    @GetMapping(value = "/{filmId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Film> get(@PathVariable Long filmId) {
        final Film film = this.filmService.findById(filmId)
                .orElseThrow(() -> new FilmNotFoundException(String.format("Film with id '%d' does not exist", filmId)));
        return ResponseEntity.ok(film);
    }

}
