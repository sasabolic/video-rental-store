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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
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
	public void whenGetAllThenReturnListOfFilms() throws Exception {
		given(this.filmService.findAll(null))
				.willReturn(FilmDataFixtures.films());

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/films")
                .accept(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
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
	public void whenQueryByTitleThenReturnListOfFilmsWithThatTitle() throws Exception {
		given(this.filmService.findAll("spider"))
				.willReturn(FilmDataFixtures.filmsWithSpiderMan());

		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.get("/films?title=spider")
				.accept(MediaType.APPLICATION_JSON);


		mockMvc.perform(requestBuilder)
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$").isArray())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].title", equalTo("Spider Man")))
				.andExpect(jsonPath("$[1].title", equalTo("Spider Man 2")));
	}

    @Test
    public void whenGetByIdThenReturnFilm() throws Exception {
		final String title = "Matrix 11";

		given(this.filmService.findById(anyLong()))
                .willReturn(FilmDataFixtures.newReleaseFilm(title));

        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/films/1")
                .accept(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.title", equalTo(title)));
    }

    @Test
    public void whenGetByNonExistingIdThenStatusNotFound() throws Exception {
		final String filmId = "1";
		final String message = "Film with id '" + filmId + "' does not exist";

		given(this.filmService.findById(anyLong()))
                .willThrow(new FilmNotFoundException(message));

		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/films/" + filmId)
                .accept(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andExpect(status().isNotFound())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo(message)));
    }

	@Test
	public void whenCreateFilmThenReturnCorrectResponse() throws Exception {
		given(this.filmService.save(isA(CreateFilmCommand.class)))
				.willReturn(FilmDataFixtures.newReleaseFilm());

		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/films")
				.content(FilmDataFixtures.json())
				.contentType(MediaType.APPLICATION_JSON);


		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$.title", equalTo("Matrix 11")))
				.andExpect(jsonPath("$.type", equalTo("NEW_RELEASE")))
				.andExpect(jsonPath("$.quantity", equalTo(10)));
	}

	@Test
	public void whenUpdateFilmThenReturnCorrectResponse() throws Exception {
		final String filmId = "1";
		final Film film = FilmDataFixtures.newReleaseFilm();

		given(this.filmService.update(isA(UpdateFilmCommand.class)))
				.willReturn(film);

		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.put("/films/" + filmId)
				.content(FilmDataFixtures.json())
				.contentType(MediaType.APPLICATION_JSON);


		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$.title", equalTo(film.getTitle())))
				.andExpect(jsonPath("$.type", equalTo(film.getType().name())))
				.andExpect(jsonPath("$.quantity", equalTo(film.getQuantity())));
	}

	@Test
	public void whenUpdateQuantityFilmThenReturnCorrectResponse() throws Exception {
		final String filmId = "1";
		final Film film = FilmDataFixtures.newReleaseFilm();

		given(this.filmService.updateQuantity(isA(UpdateFilmQuantityCommand.class)))
				.willReturn(film);

		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.patch("/films/" + filmId)
				.content(FilmDataFixtures.jsonQuantity())
				.contentType(MediaType.APPLICATION_JSON);


		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$.title", equalTo(film.getTitle())))
				.andExpect(jsonPath("$.type", equalTo(film.getType().name())))
				.andExpect(jsonPath("$.quantity", equalTo(film.getQuantity())));
	}

	@Test
	public void whenDeleteFilmThenStatusNoContentAndEmptyBody() throws Exception {
		final String filmId = "1";
		final Film film = FilmDataFixtures.newReleaseFilm();

		given(this.filmService.delete(isA(Long.class)))
				.willReturn(film);

		final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
				.delete("/films/" + filmId);

		mockMvc.perform(requestBuilder)
				.andDo(print())
				.andExpect(status().isNoContent())
				.andExpect(jsonPath("$").doesNotExist());
	}
}