package hu.informula.movie_details.controller;

import hu.informula.movie_details.model.MovieDto;
import hu.informula.movie_details.model.MovieResponse;
import hu.informula.movie_details.model.entity.SearchHistory;
import hu.informula.movie_details.repository.SearchHistoryRepository;
import hu.informula.movie_details.service.OmdbService;
import hu.informula.movie_details.service.TmdbService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OmdbService omdbService;

    @MockBean
    private TmdbService tmdbService;

    @MockBean
    private SearchHistoryRepository searchHistoryRepository;

    @Test
    void getMovieData_usesOmdbApiAndSavesHistory() throws Exception {
        MovieResponse mockResponse = new MovieResponse(List.of(
                new MovieDto("Test Title", "2024", List.of("Jane Doe"))
        ));

        when(omdbService.getMoviesByTitle("Batman")).thenReturn(mockResponse);

        mockMvc.perform(get("/movies/Batman")
                        .param("api", "omdb")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movies[0].title").value("Test Title"));

        verify(omdbService).getMoviesByTitle("Batman");

        ArgumentCaptor<SearchHistory> captor = ArgumentCaptor.forClass(SearchHistory.class);
        verify(searchHistoryRepository).save(captor.capture());
        assertThat(captor.getValue().getTitle()).isEqualTo("Batman");
        assertThat(captor.getValue().getApiUsed()).isEqualTo("omdb");
    }

    @Test
    void getMovieData_usesTmdbApi() throws Exception {
        MovieResponse mockResponse = new MovieResponse(List.of(
                new MovieDto("Some TMDB Movie", "2023", List.of("Someone"))
        ));

        when(tmdbService.getMoviesByTitle("Dune")).thenReturn(mockResponse);

        mockMvc.perform(get("/movies/Dune")
                        .param("api", "tmdb")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movies[0].title").value("Some TMDB Movie"));

        verify(tmdbService).getMoviesByTitle("Dune");
    }

    @Test
    void getMovieData_invalidApi_shouldReturn400() throws Exception {
        mockMvc.perform(get("/movies/Matrix")
                        .param("api", "invalid"))
                .andExpect(status().isBadRequest());
    }
}