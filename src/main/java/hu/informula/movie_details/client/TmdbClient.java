package hu.informula.movie_details.client;

import hu.informula.movie_details.model.tmdb.TmdbCreditsResponse;
import hu.informula.movie_details.model.tmdb.TmdbMovieDetail;
import hu.informula.movie_details.model.tmdb.TmdbSearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class TmdbClient {
    private static final Logger log = LoggerFactory.getLogger(TmdbClient.class);

    @Value("${tmdb.api.key}")
    private String apiKey;

    @Value("${tmdb.api.base-url}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public TmdbClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "tmdb-search", key = "#title")
    public TmdbSearchResponse searchMovies(String title, int page) {
        var uri = UriComponentsBuilder.newInstance()
                .uri(URI.create(baseUrl + "/search/movie"))
                .queryParam("api_key", apiKey)
                .queryParam("query", title)
                .queryParam("include_adult", true)
                .queryParam("page", page)
                .build()
                .toUri();

        log.info("Searching for movie in TMDB API, calling: {}", uri);

        return restTemplate.getForObject(uri, TmdbSearchResponse.class);
    }

    @Cacheable(value = "tmdb-detail", key = "#movieId")
    public TmdbMovieDetail getMovieDetail(int movieId) {
        var uri = UriComponentsBuilder.newInstance()
                .uri(URI.create(baseUrl + "/movie/" + movieId))
                .queryParam("api_key", apiKey)
                .build()
                .toUri();

        log.info("Getting movie details from TMDB API, calling: {}", uri);

        return restTemplate.getForObject(uri, TmdbMovieDetail.class);
    }

    @Cacheable(value = "tmdb-credits", key = "#movieId")
    public TmdbCreditsResponse getCredits(int movieId) {
        var uri = UriComponentsBuilder.newInstance()
                .uri(URI.create(baseUrl + "/movie/" + movieId + "/credits"))
                .queryParam("api_key", apiKey)
                .build()
                .toUri();

        log.info("Getting movie credits from TMDB API, calling: {}", uri);

        return restTemplate.getForObject(uri, TmdbCreditsResponse.class);
    }
}
