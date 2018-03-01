package com.example.videorentalstore.inventory.web;

import com.example.videorentalstore.inventory.Film;
import com.example.videorentalstore.inventory.NewReleaseFilm;
import com.example.videorentalstore.inventory.OldReleaseFilm;
import com.example.videorentalstore.inventory.RegularReleaseFilm;

import java.util.Arrays;

public class FilmDataFixtures {

    public static Iterable<Film> films() {
        return Arrays.asList(
                newReleaseFilm(),
                new RegularReleaseFilm("Spider Man"),
                new RegularReleaseFilm("Spider Man 2"),
                new OldReleaseFilm("Out of Africa"));
    }

    public static Film newReleaseFilm() {
        return new NewReleaseFilm("Matrix 11");
    }

    public static Film newReleaseFilm(String name) {
        return new NewReleaseFilm(name);
    }

    public static Film regularReleaseFilm() {
        return new RegularReleaseFilm("Spider Man");
    }

    public static Film regularReleaseFilm(String name) {
        return new RegularReleaseFilm(name);
    }

    public static Film oldReleaseFilm() {
        return new OldReleaseFilm("Out of Africa");
    }

    public static Film oldReleaseFilm(String name) {
        return new OldReleaseFilm(name);
    }

}
