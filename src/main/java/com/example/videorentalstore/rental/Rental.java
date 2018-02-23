package com.example.videorentalstore.rental;

import com.example.videorentalstore.inventory.Film;

import java.time.Instant;

public class Rental {

    private Long id;
    private Instant date;

    private int days;
    private int extraDays;
    private Film film;


    public Long getId() {
        return id;
    }

    public Instant getDate() {
        return date;
    }

    public int getDays() {
        return days;
    }

    public int getExtraDays() {
        return extraDays;
    }

    public Film getFilm() {
        return film;
    }


}
