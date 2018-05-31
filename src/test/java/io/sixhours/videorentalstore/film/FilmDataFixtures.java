package io.sixhours.videorentalstore.film;

import java.util.Arrays;
import java.util.List;

public class FilmDataFixtures {

    public static List<Film> films() {
        return Arrays.asList(
                newReleaseFilm("Matrix 11"),
                regularReleaseFilm("Spider Man"),
                regularReleaseFilm("Spider Man 2"),
                oldReleaseFilm("Out of Africa"));
    }

    public static List<Film> filmsWithTitleSpiderMan() {
        return Arrays.asList(
                regularReleaseFilm("Spider Man"),
                regularReleaseFilm("Spider Man 2"));
    }

    public static Film film(String title, ReleaseType type, int quantity) {
        return new Film(title, type, quantity);
    }

    public static Film newReleaseFilm() {
        return film("Maze Runner: The Death Cure", ReleaseType.NEW_RELEASE, 10);
    }

    public static Film newReleaseFilm(String title) {
        return film(title, ReleaseType.NEW_RELEASE, 10);
    }

    public static Film newReleaseFilm(String title, int quantity) {
        return film(title, ReleaseType.NEW_RELEASE, quantity);
    }

    public static Film regularReleaseFilm() {
        return film("Spider Man", ReleaseType.REGULAR_RELEASE, 10);
    }

    public static Film regularReleaseFilm(String title) {
        return film(title, ReleaseType.REGULAR_RELEASE, 10);
    }

    public static Film regularReleaseFilm(String title, int quantity) {
        return film(title, ReleaseType.REGULAR_RELEASE, quantity);
    }

    public static Film oldReleaseFilm() {
        return film("Out of Africa", ReleaseType.OLD_RELEASE, 10);
    }

    public static Film oldReleaseFilm(String title) {
        return film(title, ReleaseType.OLD_RELEASE, 10);
    }

    public static Film oldReleaseFilm(String title, int quantity) {
        return film(title, ReleaseType.OLD_RELEASE, quantity);
    }

    public static String json() {
        return "{\n" +
                "  \"title\": \"Matrix 11\",\n" +
                "  \"type\": \"NEW_RELEASE\",\n" +
                "  \"quantity\": 10\n" +
                "}";
    }

    public static String jsonQuantity() {
        return "{\n" +
                "  \"quantity\": 2\n" +
                "}";
    }
}
