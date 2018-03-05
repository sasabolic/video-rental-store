package com.example.videorentalstore.film;

import java.util.Arrays;
import java.util.List;

public class FilmDataFixtures {

    public static List<Film> films() {
        return Arrays.asList(
                newReleaseFilm(),
                new Film("Spider Man", ReleaseType.REGULAR_RELEASE),
                new Film("Spider Man 2", ReleaseType.REGULAR_RELEASE),
                new Film("Out of Africa", ReleaseType.OLD_RELEASE));
    }

    public static List<Film> filmsWithSpiderMan() {
        return Arrays.asList(
                new Film("Spider Man", ReleaseType.REGULAR_RELEASE),
                new Film("Spider Man 2", ReleaseType.REGULAR_RELEASE));
    }

    public static Film newReleaseFilm() {
        return new Film("Matrix 11", ReleaseType.NEW_RELEASE, 10);
    }

    public static Film newReleaseFilm(String name) {
        return new Film(name, ReleaseType.NEW_RELEASE, 10);
    }

    public static Film newReleaseFilm(String name, int quantity) {
        return new Film(name, ReleaseType.NEW_RELEASE, quantity);
    }

    public static Film regularReleaseFilm() {
        return new Film("Spider Man", ReleaseType.REGULAR_RELEASE, 10);
    }

    public static Film regularReleaseFilm(String name) {
        return new Film(name, ReleaseType.REGULAR_RELEASE, 10);
    }

    public static Film regularReleaseFilm(String name, int quantity) {
        return new Film(name, ReleaseType.REGULAR_RELEASE, quantity);
    }

    public static Film oldReleaseFilm() {
        return new Film("Out of Africa", ReleaseType.OLD_RELEASE, 10);
    }

    public static Film oldReleaseFilm(String name) {
        return new Film(name, ReleaseType.OLD_RELEASE, 10);
    }

    public static Film oldReleaseFilm(String name, int quantity) {
        return new Film(name, ReleaseType.OLD_RELEASE, quantity);
    }

}
