package hu.informula.movie_details.service;

import hu.informula.movie_details.client.OmdbClient;
import hu.informula.movie_details.model.MovieDto;
import hu.informula.movie_details.model.MovieResponse;
import hu.informula.movie_details.model.omdb.OmdbMovieDetailDto;
import hu.informula.movie_details.model.omdb.OmdbSearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OmdbServiceTest {


    private OmdbClient omdbClient;
    private OmdbService omdbService;

    @BeforeEach
    void setUp() {
        omdbClient = mock(OmdbClient.class);
        omdbService = new OmdbService(omdbClient);
    }

    @Test
    void getMoviesByTitle_shouldReturnCorrectMovieResponse() {
        // Arrange
        var imdbId = "tt123456";
        var searchDto = new OmdbMovieDetailDto("Test Movie", "2023", imdbId, "movie", "", null);
        var searchResponse = new OmdbSearchResponse("True", List.of(searchDto), "1");

        when(omdbClient.searchByTitle("Test", 1)).thenReturn(searchResponse);

        var detailDto = new OmdbMovieDetailDto("Test Movie", "2023", imdbId, "movie", "", "John Doe, Jane Doe");
        when(omdbClient.getDetailsByImdbId(imdbId)).thenReturn(detailDto);

        MovieResponse response = omdbService.getMoviesByTitle("Test");

        assertNotNull(response);
        assertEquals(1, response.movies().size());

        MovieDto movie = response.movies().get(0);
        assertEquals("Test Movie", movie.title());
        assertEquals("2023", movie.year());
        assertEquals(List.of("John Doe", "Jane Doe"), movie.director());

        verify(omdbClient, times(1)).searchByTitle("Test", 1);
        verify(omdbClient, times(1)).getDetailsByImdbId(imdbId);
    }

    @Test
    void getMoviesByTitle_withEmptyResults_shouldReturnEmptyList() {
        var searchResponse = new OmdbSearchResponse("True", List.of(), "0");

        when(omdbClient.searchByTitle("Nothing", 1)).thenReturn(searchResponse);

        MovieResponse response = omdbService.getMoviesByTitle("Nothing");

        assertNotNull(response);
        assertTrue(response.movies().isEmpty());

        verify(omdbClient).searchByTitle("Nothing", 1);
        verifyNoMoreInteractions(omdbClient);
    }

}