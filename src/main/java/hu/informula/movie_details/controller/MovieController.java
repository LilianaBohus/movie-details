package hu.informula.movie_details.controller;

import hu.informula.movie_details.model.enums.MovieApi;
import hu.informula.movie_details.model.MovieResponse;
import hu.informula.movie_details.model.entity.SearchHistory;
import hu.informula.movie_details.repository.SearchHistoryRepository;
import hu.informula.movie_details.service.OmdbService;
import hu.informula.movie_details.service.TmdbService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/movies")
public class MovieController {
    private static final Logger log = LoggerFactory.getLogger(MovieController.class);

    private final OmdbService omdbService;
    private final TmdbService tmdbService;
    private final SearchHistoryRepository searchHistoryRepository;

    public MovieController(OmdbService omdbService, TmdbService tmdbService, SearchHistoryRepository searchHistoryRepository) {
        this.omdbService = omdbService;
        this.tmdbService = tmdbService;
        this.searchHistoryRepository = searchHistoryRepository;
    }

    @GetMapping("/{title}")
    public ResponseEntity<MovieResponse> getMovieData(
            @PathVariable String title,
            @RequestParam String api) {

        log.info("Request received for movie: '{}' using API: '{}'", title, api);

        saveSearchHistory(title, api);

        try {
            return ResponseEntity.ok(
                    switch (MovieApi.from(api)) {
                        case OMDB -> omdbService.getMoviesByTitle(title);
                        case TMDB -> tmdbService.getMoviesByTitle(title);
                    });
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build(); // or custom message
        }
    }

    private void saveSearchHistory(String title, String api) {
        searchHistoryRepository.save(
                new SearchHistory(title, api, LocalDateTime.now())
        );
    }
}
