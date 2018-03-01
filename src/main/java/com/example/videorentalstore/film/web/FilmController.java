package com.example.videorentalstore.inventory.web;

import com.example.videorentalstore.inventory.Film;
import com.example.videorentalstore.inventory.NewReleaseFilm;
import com.example.videorentalstore.inventory.OldReleaseFilm;
import com.example.videorentalstore.inventory.RegularReleaseFilm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

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
