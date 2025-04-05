package hu.informula.movie_details.client;

import hu.informula.movie_details.model.tmdb.TmdbCreditsResponse;
import hu.informula.movie_details.model.tmdb.TmdbMovieDetail;
import hu.informula.movie_details.model.tmdb.TmdbSearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TmdbClientTest {

    private RestTemplate restTemplate;
    private TmdbClient tmdbClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        tmdbClient = new TmdbClient(restTemplate);

        tmdbClient.getClass().getDeclaredFields();
        setField(tmdbClient, "apiKey", "FAKE_KEY");
        setField(tmdbClient, "baseUrl", "https://api.fake-tmdb.org/3");
    }

    @Test
    void searchMovies_shouldBuildCorrectUriAndReturnResponse() {
        var expectedResponse = new TmdbSearchResponse(null, 1);
        when(restTemplate.getForObject(any(URI.class), eq(TmdbSearchResponse.class)))
                .thenReturn(expectedResponse);

        var response = tmdbClient.searchMovies("Barbie", 2);

        assertNotNull(response);
        verify(restTemplate).getForObject(any(URI.class), eq(TmdbSearchResponse.class));
    }

    @Test
    void getMovieDetail_shouldCallCorrectEndpoint() {
        var expected = new TmdbMovieDetail("Barbie", "2023-07-21", 1);
        when(restTemplate.getForObject(any(URI.class), eq(TmdbMovieDetail.class)))
                .thenReturn(expected);

        var detail = tmdbClient.getMovieDetail(1234);

        assertEquals("Barbie", detail.title());
        verify(restTemplate).getForObject(any(URI.class), eq(TmdbMovieDetail.class));
    }

    @Test
    void getCredits_shouldCallCorrectEndpoint() {
        var credits = new TmdbCreditsResponse(null);
        when(restTemplate.getForObject(any(URI.class), eq(TmdbCreditsResponse.class)))
                .thenReturn(credits);

        var result = tmdbClient.getCredits(1234);

        assertNotNull(result);
        verify(restTemplate).getForObject(any(URI.class), eq(TmdbCreditsResponse.class));
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            var field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}