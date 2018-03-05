package com.example.videorentalstore.film.web;

import com.example.videorentalstore.AbstractWebIntTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

public class FilmControllerIntTest extends AbstractWebIntTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void whenRequestForFilmsThenReturnList() throws IOException, URISyntaxException {
        final ResponseEntity<String> response = restTemplate.getForEntity(createURIWithPort("/films"), String.class);

        System.out.println("Response: " + response);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualByComparingTo(MediaType.APPLICATION_JSON_UTF8);
        assertThat(response.getBody()).isEqualTo(json("films.json"));
    }

    @Test
    public void whenQueryForSpecificFilmThenReturnListContainingThatName() throws IOException, URISyntaxException {
        final ResponseEntity<String> response = restTemplate.getForEntity(createURIWithPort("/films?name={name}"), String.class, "spider");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
        assertThat(response.getHeaders().getContentType()).isEqualByComparingTo(MediaType.APPLICATION_JSON_UTF8);
        assertThat(response.getBody()).isEqualTo(json("films_spider.json"));
    }

    @Test
    public void whenGetByIdThenRuntimeException() throws Exception {

        final ResponseEntity<String> response = restTemplate.getForEntity(createURIWithPort("/films/10"), String.class);

        System.out.println("RESPONSE: " +response);
    }


}
