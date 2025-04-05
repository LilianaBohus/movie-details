package hu.informula.movie_details.client;

import hu.informula.movie_details.model.omdb.OmdbMovieDetailDto;
import hu.informula.movie_details.model.omdb.OmdbSearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class OmdbClient {
    private static final Logger log = LoggerFactory.getLogger(OmdbClient.class);

    @Value("${omdb.api.key}")
    private String apiKey;

    @Value("${omdb.api.base-url}")
    private String baseUrl;

    // TODO could use FeignClient instead of RestTemplate - would make it simpler
    private final RestTemplate restTemplate;

    public OmdbClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "search-results", key = "#title + '_' + #page")
    public OmdbSearchResponse searchByTitle(String title, int page) {
        var uri = UriComponentsBuilder.newInstance()
                .uri(URI.create(baseUrl))
                .queryParam("apikey", apiKey)
                .queryParam("s", title)
                .queryParam("page", page)
                .build()
                .toUri();

        log.info("Searching for movie in OMDb API, calling: {}", uri);
        return restTemplate.getForObject(uri, OmdbSearchResponse.class);
    }

    @Cacheable(value = "movie-details", key = "#imdbId")
    public OmdbMovieDetailDto getDetailsByImdbId(String imdbId) {
        var uri = UriComponentsBuilder.newInstance()
                .uri(URI.create(baseUrl))
                .queryParam("apikey", apiKey)
                .queryParam("i", imdbId)
                .toUriString();

        log.info("Getting movie details from OMDb API, calling: {}", uri);
        return restTemplate.getForObject(uri, OmdbMovieDetailDto.class);
    }
}
