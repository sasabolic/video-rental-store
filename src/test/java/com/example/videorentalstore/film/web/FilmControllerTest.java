package com.example.videorentalstore.film.web;

import com.example.videorentalstore.film.*;
import com.example.videorentalstore.film.web.dto.assembler.DefaultFilmResponseAssembler;
import com.example.videorentalstore.film.web.dto.assembler.FilmResponseAssembler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = FilmController.class)
public class FilmControllerTest {

    @TestConfiguration
    static class TestConfig {

        @Bean
        public FilmResponseAssembler filmResponseAssembler() {
            return new DefaultFilmResponseAssembler();
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmService filmService;

    @Test
    public void whenGetAllThenReturnStatusOK() throws Exception {
        given(this.filmService.findAll(null))
                .willReturn(FilmDataFixtures.films());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/films")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetAllThenReturnJsonList() throws Exception {
        given(this.filmService.findAll(null))
                .willReturn(FilmDataFixtures.films());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/films")
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].title", equalTo("Matrix 11")))
                .andExpect(jsonPath("$[1].title", equalTo("Spider Man")))
                .andExpect(jsonPath("$[2].title", equalTo("Spider Man 2")))
                .andExpect(jsonPath("$[3].title", equalTo("Out of Africa")));
    }

    @Test
    public void whenGetAllByTitleThenReturnJsonListContainingThatTitle() throws Exception {
        final String title = "spider";

        given(this.filmService.findAll(title))
                .willReturn(FilmDataFixtures.filmsWithTitleSpiderMan());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/films?title={title}", title)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", equalTo("Spider Man")))
                .andExpect(jsonPath("$[1].title", equalTo("Spider Man 2")));
    }

    @Test
    public void whenGetAllByNonExistingTitleThenReturnEmptyJsonList() throws Exception {
        final String title = "non-existing";

        given(this.filmService.findAll(title)).willReturn(Collections.emptyList());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/films?title={title}", title)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void whenGetByIdThenReturnStatusOK() throws Exception {
        given(this.filmService.findById(anyLong())).willReturn(FilmDataFixtures.newReleaseFilm());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/films/{filmId}", 1L)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenGetByIdThenReturnJson() throws Exception {
        final String title = "Matrix 11";

        given(this.filmService.findById(anyLong())).willReturn(FilmDataFixtures.newReleaseFilm(title));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/films/{filmsId}", 1L)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.title", equalTo(title)));
    }

    @Test
    public void whenGetNonExistingThenReturnStatusNotFound() throws Exception {
        final Long filmId = 1L;
        final String message = "Film with id '" + filmId + "' does not exist";

        given(this.filmService.findById(anyLong()))
                .willThrow(new FilmNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/films/{filmId}", filmId)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetNonExistingThenReturnJsonError() throws Exception {
        final Long filmId = 1L;
        final String message = "Film with id '" + filmId + "' does not exist";

        given(this.filmService.findById(anyLong()))
                .willThrow(new FilmNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/films/{filmId}", filmId)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }

    @Test
    public void whenCreateThenReturnStatusOK() throws Exception {
        given(this.filmService.save(isA(CreateFilmCommand.class)))
                .willReturn(FilmDataFixtures.newReleaseFilm());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/films")
                .content(FilmDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenCreateThenReturnJson() throws Exception {
        final String title = "Matrix 11";
        final ReleaseType type = ReleaseType.NEW_RELEASE;
        final int quantity = 10;

        given(this.filmService.save(isA(CreateFilmCommand.class)))
                .willReturn(FilmDataFixtures.film(title, type, quantity));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/films")
                .content(FilmDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.title", equalTo(title)))
                .andExpect(jsonPath("$.type", equalTo(type.name())))
                .andExpect(jsonPath("$.quantity", equalTo(quantity)));
    }

    @Test
    public void whenCreateWithDuplicateTitleThenReturnStatusConflict() throws Exception {
        final String title = "Matrix 11";
        final String message = "Film with title '" + title + "' already exits";

        given(this.filmService.save(isA(CreateFilmCommand.class))).willThrow(new FilmUniqueViolationException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/films")
                .content(FilmDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void whenCreateWithDuplicateTitleThenReturnJsonError() throws Exception {
        final String title = "Matrix 11";
        final String message = "Film with title '" + title + "' already exits";

        given(this.filmService.save(isA(CreateFilmCommand.class))).willThrow(new FilmUniqueViolationException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/films")
                .content(FilmDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(409)))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }

    @Test
    public void whenCreateEmptyRequestThenReturnStatusBadRequest() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/films")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenCreateEmptyRequestThenReturnJsonError() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post("/films")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)));
    }

    @Test
    public void whenUpdateThenReturnStatusOK() throws Exception {
        given(this.filmService.update(isA(UpdateFilmCommand.class)))
                .willReturn(FilmDataFixtures.oldReleaseFilm());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/films/{filmId}", 1L)
                .content(FilmDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateThenReturnJson() throws Exception {
        final String title = "Matrix 11";
        final ReleaseType type = ReleaseType.NEW_RELEASE;
        final int quantity = 10;

        given(this.filmService.update(isA(UpdateFilmCommand.class)))
                .willReturn(FilmDataFixtures.film(title, type, quantity));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/films/{filmId}", 1L)
                .content(FilmDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.title", equalTo(title)))
                .andExpect(jsonPath("$.type", equalTo(type.name())))
                .andExpect(jsonPath("$.quantity", equalTo(quantity)));
    }

    @Test
    public void whenUpdateNonExistingThenReturnStatusNotFound() throws Exception {
        final Long filmId = 1L;
        final String message = "Film with id '" + filmId + "' does not exist";

        given(this.filmService.update(isA(UpdateFilmCommand.class))).willThrow(new FilmNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/films/{filmId}", filmId)
                .content(FilmDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateWithDuplicateTitleThenReturnStatusConflict() throws Exception {
        final String title = "Matrix 11";
        final String message = "Film with title '" + title + "' already exits";

        given(this.filmService.update(isA(UpdateFilmCommand.class))).willThrow(new FilmUniqueViolationException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/films/{filmId}", 1L)
                .content(FilmDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void whenUpdateWithDuplicateTitleThenReturnJsonError() throws Exception {
        final String title = "Matrix 11";
        final String message = "Film with title '" + title + "' already exits";

        given(this.filmService.update(isA(UpdateFilmCommand.class))).willThrow(new FilmUniqueViolationException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/films/{filmId}", 1L)
                .content(FilmDataFixtures.json())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(409)))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }

    @Test
    public void whenUpdateEmptyRequestThenReturnStatusBadRequest() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/films/{filmId}", 1L)
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUpdateEmptyRequestThenReturnJsonError() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .put("/films/{filmId}", 1L)
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(3)));
    }

    @Test
    public void whenUpdateQuantityThenReturnStatusOK() throws Exception {
        given(this.filmService.updateQuantity(isA(UpdateFilmQuantityCommand.class)))
                .willReturn(FilmDataFixtures.newReleaseFilm());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/films/{filmId}", 1L)
                .content(FilmDataFixtures.jsonQuantity())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void whenUpdateQuantityThenReturnJson() throws Exception {
        final int quantity = 10;

        given(this.filmService.updateQuantity(isA(UpdateFilmQuantityCommand.class)))
                .willReturn(FilmDataFixtures.newReleaseFilm("Matrix 11", quantity));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/films/{filmId}", 1L)
                .content(FilmDataFixtures.jsonQuantity())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.quantity", equalTo(quantity)));
    }

    @Test
    public void whenUpdateQuantityNonExistingThenReturnStatusNotFound() throws Exception {
        final Long filmId = 1L;
        final String message = "Film with id '" + filmId + "' does not exist";

        given(this.filmService.updateQuantity(isA(UpdateFilmQuantityCommand.class))).willThrow(new FilmNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/films/{filmId}", 1L)
                .content(FilmDataFixtures.jsonQuantity())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenUpdateQuantityEmptyRequestThenReturnStatusBadRequest() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/films/{filmId}", 1L)
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenUpdateQuantityEmptyRequestThenReturnJsonError() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch("/films/{filmId}", 1L)
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(400)))
                .andExpect(jsonPath("$.message", equalTo("Validation failed")))
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors", hasSize(1)));
    }

    @Test
    public void whenDeleteThenReturnStatusNoContent() throws Exception {
        final Long filmId = 1L;

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/films/{filmId}", filmId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    public void whenDeleteThenReturnEmptyBody() throws Exception {
        final Long filmdId = 1L;

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/films/{filmId}", filmdId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void whenDeleteNonExistingThenReturnStatusNotFound() throws Exception {
        final Long filmId = 1L;
        final String message = "Film with id '" + filmId + "' does not exist";

        given(this.filmService.delete(anyLong())).willThrow(new FilmNotFoundException(message));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .delete("/films/{filmId}", filmId);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}