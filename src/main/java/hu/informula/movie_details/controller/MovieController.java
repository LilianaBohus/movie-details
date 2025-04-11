package hu.informula.movie_details.controller;

import hu.informula.movie_details.model.enums.MovieApi;
import hu.informula.movie_details.model.MovieResponse;
import hu.informula.movie_details.model.entity.SearchHistory;
import hu.informula.movie_details.repository.SearchHistoryRepository;
import hu.informula.movie_details.service.MovieService;
import hu.informula.movie_details.service.OmdbService;
import hu.informula.movie_details.service.TmdbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private static final Logger log = LoggerFactory.getLogger(MovieController.class);

    private final List<MovieService> movieServices;
    private final SearchHistoryRepository searchHistoryRepository;

    public MovieController(List<MovieService> movieServices, SearchHistoryRepository searchHistoryRepository) {
        this.movieServices = movieServices;
        this.searchHistoryRepository = searchHistoryRepository;
    }


    @GetMapping("/{title}")
    public ResponseEntity<MovieResponse> getMovieData(
            @PathVariable String title,
            @RequestParam String api) {

        log.info("Request received for movie: '{}' using API: '{}'", title, api);

        saveSearchHistory(title, api);

        return movieServices.stream()
                .filter(service -> service.canHandle(MovieApi.from(api)))
                .findFirst()
                .map(service -> ResponseEntity.ok(service.getMoviesByTitle(title)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    private void saveSearchHistory(String title, String api) {
        searchHistoryRepository.save(
                new SearchHistory(title, api, LocalDateTime.now())
        );
    }
}
