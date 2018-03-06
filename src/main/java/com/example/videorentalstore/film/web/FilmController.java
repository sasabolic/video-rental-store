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

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<FilmResponse>> getAll(@RequestParam(required = false) String name) {
        final List<Film> result;

        if (name == null) {
            result = this.filmService.findAll();
        } else {
            result = this.filmService.findAllByName(name);
        }

        return ResponseEntity.ok(this.filmResponseAssembler.of(result));
    }

    @GetMapping(value = "/{filmId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<FilmResponse> get(@PathVariable Long filmId) {
        final Film film = this.filmService.findById(filmId);

        return ResponseEntity.ok(this.filmResponseAssembler.of(film));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<FilmResponse> create(@RequestBody WriteFilmRequest writeFilmRequest) {
        final Film film = this.filmService.save(new CreateFilmCommand(writeFilmRequest.getName(), writeFilmRequest.getType(), writeFilmRequest.getQuantity()));

        return ResponseEntity.ok(this.filmResponseAssembler.of(film));
    }
}
