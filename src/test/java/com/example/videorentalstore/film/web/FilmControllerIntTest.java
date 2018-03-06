package com.example.videorentalstore.film.web;

import com.example.videorentalstore.AbstractWebIntTest;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class FilmControllerIntTest extends AbstractWebIntTest {

    @Test
    public void whenRequestForFilmsThenReturnList() throws Exception {

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
                .andExpect(jsonPath("$[0].type", equalTo("NEW_RELEASE")))
                .andExpect(jsonPath("$[1].title", equalTo("Spider Man")))
                .andExpect(jsonPath("$[1].type", equalTo("REGULAR_RELEASE")))
                .andExpect(jsonPath("$[2].title", equalTo("Spider Man 2")))
                .andExpect(jsonPath("$[2].type", equalTo("REGULAR_RELEASE")))
                .andExpect(jsonPath("$[3].title", equalTo("Out of Africa")))
                .andExpect(jsonPath("$[3].type", equalTo("OLD_RELEASE")));

    }

    @Test
    public void whenQueryForSpecificFilmThenReturnListContainingThatTitle() throws Exception {
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/films?title={title}", "spider")
                .accept(MediaType.APPLICATION_JSON);


        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].title", equalTo("Spider Man")))
                .andExpect(jsonPath("$[0].type", equalTo("REGULAR_RELEASE")))
                .andExpect(jsonPath("$[1].title", equalTo("Spider Man 2")))
                .andExpect(jsonPath("$[1].type", equalTo("REGULAR_RELEASE")));

    }

    @Test
    public void whenGetByNonExistingIdThenRuntimeException() throws Exception {
        final long nonExistingId = 10L;
        final MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get("/films/{filmId}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status", equalTo(404)))
                .andExpect(jsonPath("$.message", equalTo("Film with id '" + nonExistingId + "' does not exist")));

    }

}
