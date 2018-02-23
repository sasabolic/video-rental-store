package com.example.videorentalstore.inventory;

import java.time.Instant;

public class Film {

    private Long id;
    private String name;
    private Instant releaseDate;
    private int count;


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Instant getReleaseDate() {
        return releaseDate;
    }

    public int getCount() {
        return count;
    }
}
