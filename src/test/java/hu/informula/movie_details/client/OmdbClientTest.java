package hu.informula.movie_details.client;

import static org.junit.jupiter.api.Assertions.*;

import hu.informula.movie_details.model.omdb.OmdbMovieDetailDto;
import hu.informula.movie_details.model.omdb.OmdbSearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.List;

import static org.mockito.Mockito.*;

class OmdbClientTest {

    private RestTemplate restTemplate;
    private OmdbClient omdbClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        omdbClient = new OmdbClient(restTemplate);

        setField(omdbClient, "apiKey", "FAKE_KEY");
        setField(omdbClient, "baseUrl", "https://fake.omdbapi.com/");
    }

    @Test
    void searchByTitle_shouldCallCorrectUrlAndReturnResponse() {
        OmdbSearchResponse mockResponse = new OmdbSearchResponse("True", List.of(), "1");
        when(restTemplate.getForObject(any(URI.class), eq(OmdbSearchResponse.class)))
                .thenReturn(mockResponse);

        OmdbSearchResponse response = omdbClient.searchByTitle("Matrix", 2);

        assertNotNull(response);
        assertEquals("True", response.response());

        verify(restTemplate).getForObject(any(URI.class), eq(OmdbSearchResponse.class));
    }

    @Test
    void getDetailsByImdbId_shouldCallCorrectUrlAndReturnDetail() {
        OmdbMovieDetailDto mockDetail = new OmdbMovieDetailDto("Matrix", "1999", "tt0133093", "movie", "", "Wachowski");
        when(restTemplate.getForObject(anyString(), eq(OmdbMovieDetailDto.class)))
                .thenReturn(mockDetail);

        OmdbMovieDetailDto result = omdbClient.getDetailsByImdbId("tt0133093");

        assertNotNull(result);
        assertEquals("Matrix", result.title());

        verify(restTemplate).getForObject(anyString(), eq(OmdbMovieDetailDto.class));
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