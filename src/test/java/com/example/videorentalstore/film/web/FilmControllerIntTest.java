package com.example.videorentalstore.film.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmControllerIntTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

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

    private String createURIWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }

    private String json(String fileName) throws URISyntaxException, IOException {
        Path path = Paths.get(getClass().getClassLoader().getResource(fileName).toURI());

        StringBuilder data = new StringBuilder();
        Stream<String> lines = Files.lines(path);
        lines.forEach(line -> data.append(line).append("\n"));
        lines.close();

        ObjectMapper mapper = new ObjectMapper();
        Object jsonObject = mapper.readValue(data.toString(), Object.class);
        String json = mapper.writeValueAsString(jsonObject);
        System.out.println(json);

        return json;
    }
}
