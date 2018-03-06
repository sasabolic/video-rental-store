package com.example.videorentalstore.film.web;

import com.example.videorentalstore.film.*;
import com.example.videorentalstore.film.web.dto.*;
import com.example.videorentalstore.film.web.dto.assembler.FilmResponseAssembler;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final FilmResponseAssembler filmResponseAssembler;

    public FilmController(FilmService filmService, FilmResponseAssembler filmResponseAssembler) {
        this.filmService = filmService;
        this.filmResponseAssembler = filmResponseAssembler;
    }

    @GetMapping
    public ResponseEntity<List<FilmResponse>> getAll(@RequestParam(required = false) String title) {
        final List<Film> result;

        if (title == null) {
            result = this.filmService.findAll();
        } else {
            result = this.filmService.findAllByTitle(title);
        }

        return ResponseEntity.ok(this.filmResponseAssembler.of(result));
    }

    @GetMapping(value = "/{filmId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<FilmResponse> get(@PathVariable Long filmId) {
        final Film film = this.filmService.findById(filmId);

        return ResponseEntity.ok(this.filmResponseAssembler.of(film));
    }

    @PostMapping
    public ResponseEntity<FilmResponse> create(@RequestBody WriteFilmRequest writeFilmRequest) {
        final Film film = this.filmService.save(new CreateFilmCommand(writeFilmRequest.getTitle(), writeFilmRequest.getType(), writeFilmRequest.getQuantity()));

        return ResponseEntity.ok(this.filmResponseAssembler.of(film));
    }

    @PutMapping(value = "/{filmId}")
    public ResponseEntity<FilmResponse> update(@PathVariable Long filmId, @RequestBody WriteFilmRequest writeFilmRequest) {
        final Film film = this.filmService.update(new UpdateFilmCommand(filmId, writeFilmRequest.getTitle(), writeFilmRequest.getType(), writeFilmRequest.getQuantity()));

        return ResponseEntity.ok(this.filmResponseAssembler.of(film));
    }

    @PatchMapping(value = "/{filmId}")
    public ResponseEntity<FilmResponse> updateQuantity(@PathVariable Long filmId, @RequestBody UpdateFilmQuantityRequest updateFilmQuantityRequest) {
        final Film film = this.filmService.updateQuantity(new UpdateFilmQuantityCommand(filmId, updateFilmQuantityRequest.getQuantity()));

        return ResponseEntity.ok(this.filmResponseAssembler.of(film));
    }

    @DeleteMapping(value = "/{filmId}")
    public ResponseEntity<Void> delete(@PathVariable Long filmId) {
        this.filmService.delete(filmId);

        return ResponseEntity.noContent().build();
    }
}
