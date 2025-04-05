package hu.informula.movie_details.service;

import hu.informula.movie_details.client.TmdbClient;
import hu.informula.movie_details.model.MovieDto;
import hu.informula.movie_details.model.MovieResponse;
import hu.informula.movie_details.model.tmdb.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TmdbServiceTest {
    private TmdbClient tmdbClient;
    private TmdbService tmdbService;

    @BeforeEach
    void setUp() {
        tmdbClient = mock(TmdbClient.class);
        tmdbService = new TmdbService(tmdbClient);
    }

    @Test
    void getMoviesByTitle_shouldReturnValidMovieDto() {
        int movieId = 42;
        String title = "Barbie";
        String releaseDate = "2023-07-21";
        List<TmdbCrewMember> crew = List.of(
                new TmdbCrewMember("Director", "Greta Gerwig"),
                new TmdbCrewMember("Producer", "Margot Robbie")
        );

        var searchResult = new TmdbSearchResult(movieId);
        var searchResponse = new TmdbSearchResponse(List.of(searchResult), 1);
        var movieDetail = new TmdbMovieDetail(title, releaseDate, 1);
        var creditsResponse = new TmdbCreditsResponse(crew);

        when(tmdbClient.searchMovies("Barbie", 1)).thenReturn(searchResponse);
        when(tmdbClient.getMovieDetail(movieId)).thenReturn(movieDetail);
        when(tmdbClient.getCredits(movieId)).thenReturn(creditsResponse);

        MovieResponse response = tmdbService.getMoviesByTitle("Barbie");

        assertNotNull(response);
        assertEquals(1, response.movies().size());

        MovieDto movie = response.movies().get(0);
        assertEquals("Barbie", movie.title());
        assertEquals("2023", movie.year());
        assertEquals(List.of("Greta Gerwig"), movie.director());
    }

    @Test
    void getMoviesByTitle_withNoResults_shouldReturnEmptyList() {
        var searchResponse = new TmdbSearchResponse(List.of(), 1);
        when(tmdbClient.searchMovies("Unknown", 1)).thenReturn(searchResponse);

        MovieResponse response = tmdbService.getMoviesByTitle("Unknown");

        assertNotNull(response);
        assertTrue(response.movies().isEmpty());

        verify(tmdbClient).searchMovies("Unknown", 1);
        verifyNoMoreInteractions(tmdbClient);
    }
}