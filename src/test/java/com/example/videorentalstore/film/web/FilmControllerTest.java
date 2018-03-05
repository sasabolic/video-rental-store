package com.example.videorentalstore.film.web;

import com.example.videorentalstore.film.FilmDataFixtures;
import com.example.videorentalstore.film.FilmService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@WebMvcTest(value = FilmController.class)
public class FilmControllerTest {

    @Autowired
	private MockMvc mockMvc;

    @MockBean
	private FilmService filmService;

	@Test
	public void whenGetAllThenReturnListOfFilms() throws Exception {

		given(this.filmService.findAll())
				.willReturn(FilmDataFixtures.films());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/films")
                .accept(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(4)))
				.andExpect(jsonPath("$[0].name", equalTo("Matrix 11")))
				.andExpect(jsonPath("$[1].name", equalTo("Spider Man")))
				.andExpect(jsonPath("$[2].name", equalTo("Spider Man 2")))
				.andExpect(jsonPath("$[3].name", equalTo("Out of Africa")));

	}

	@Test
	public void whenQueryByNameThenReturnListOfFilmsWithThatName() throws Exception {

		given(this.filmService.findAllByName("spider"))
				.willReturn(FilmDataFixtures.filmsWithSpiderMan());

		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/films?name=spider")
				.accept(MediaType.APPLICATION_JSON);


		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].name", equalTo("Spider Man")))
				.andExpect(jsonPath("$[1].name", equalTo("Spider Man 2")));

	}

    @Test
    public void whenGetByIdThenReturnFilm() throws Exception {

        given(this.filmService.findById(anyLong()))
                .willReturn(Optional.of(FilmDataFixtures.newReleaseFilm("Matrix 11")));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/films/1")
                .accept(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name", equalTo("Matrix 11")));
    }

    @Test
    public void whenGetByNonExistingIdThenStatusNotFound() throws Exception {

        given(this.filmService.findById(anyLong()))
                .willReturn(Optional.ofNullable(null));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/films/1")
                .accept(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("Film with id '1' does not exist")));
    }



}