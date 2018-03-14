package com.example.videorentalstore.film.web;

import com.example.videorentalstore.film.Film;
import com.example.videorentalstore.film.FilmService;
import com.example.videorentalstore.film.web.dto.FilmResponse;
import com.example.videorentalstore.film.web.dto.SaveFilmRequest;
import com.example.videorentalstore.film.web.dto.UpdateFilmQuantityRequest;
import com.example.videorentalstore.film.web.dto.assembler.FilmResponseAssembler;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


/**
 * REST film resources.
 */
@RestController
@RequestMapping("/films")
@Api(tags = "film")
public class FilmController {

    private final FilmService filmService;
    private final FilmResponseAssembler filmResponseAssembler;

    public FilmController(FilmService filmService, FilmResponseAssembler filmResponseAssembler) {
        this.filmService = filmService;
        this.filmResponseAssembler = filmResponseAssembler;
    }

    @GetMapping
    public ResponseEntity<List<FilmResponse>> getAll(@RequestParam(required = false) String title) {
        final List<Film> films = this.filmService.findAll(title);

        return ResponseEntity.ok(this.filmResponseAssembler.of(films));
    }

    @GetMapping(value = "/{filmId}")
    public ResponseEntity<FilmResponse> get(@PathVariable Long filmId) {
        final Film film = this.filmService.findById(filmId);

        return ResponseEntity.ok(this.filmResponseAssembler.of(film));
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid SaveFilmRequest saveFilmRequest) {
        final Film film = this.filmService.save(saveFilmRequest.getTitle(), saveFilmRequest.getType(), saveFilmRequest.getQuantity());

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{filmId}")
                .buildAndExpand(film.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{filmId}")
    public ResponseEntity<FilmResponse> update(@PathVariable Long filmId, @RequestBody @Valid SaveFilmRequest saveFilmRequest) {
        final Film film = this.filmService.update(filmId, saveFilmRequest.getTitle(), saveFilmRequest.getType(), saveFilmRequest.getQuantity());

        return ResponseEntity.ok(this.filmResponseAssembler.of(film));
    }

    @PatchMapping(value = "/{filmId}")
    public ResponseEntity<FilmResponse> updateQuantity(@PathVariable Long filmId, @RequestBody @Valid UpdateFilmQuantityRequest updateFilmQuantityRequest) {
        final Film film = this.filmService.updateQuantity(filmId, updateFilmQuantityRequest.getQuantity());

        return ResponseEntity.ok(this.filmResponseAssembler.of(film));
    }

    @DeleteMapping(value = "/{filmId}")
    public ResponseEntity<Void> delete(@PathVariable Long filmId) {
        this.filmService.delete(filmId);

        return ResponseEntity.noContent().build();
    }
}
