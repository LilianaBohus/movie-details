package hu.informula.movie_details.service;

import hu.informula.movie_details.client.TmdbClient;
import hu.informula.movie_details.model.MovieDto;
import hu.informula.movie_details.model.MovieResponse;
import hu.informula.movie_details.model.enums.MovieApi;
import hu.informula.movie_details.model.paging.PageResult;
import hu.informula.movie_details.util.PagingUtil;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static hu.informula.movie_details.util.ParseUtil.extractDirectorsFromTmdbCredits;
import static hu.informula.movie_details.util.ParseUtil.extractYearFromDate;

@Service
public class TmdbService implements MovieService {
    private final TmdbClient tmdbClient;

    public TmdbService(TmdbClient tmdbClient) {
        this.tmdbClient = tmdbClient;
    }

    @Override
    public MovieResponse getMoviesByTitle(String title) {
        var searchResults = PagingUtil.fetchAllParallel(page -> {
            var response = tmdbClient.searchMovies(title, page);
            return new PageResult<>(response.results(), response.totalPages());
        });

        var futureMovies = searchResults.stream()
                .map(result -> CompletableFuture.supplyAsync(() -> {
                    var detail = tmdbClient.getMovieDetail(result.id());
                    var credits = tmdbClient.getCredits(result.id());

                    var directors = extractDirectorsFromTmdbCredits(credits);
                    var year = extractYearFromDate(detail.releaseDate());

                    return new MovieDto(detail.title(), year, directors);
                }))
                .toList();

        var movies = futureMovies.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        return new MovieResponse(movies);
    }

    @Override
    public boolean canHandle(MovieApi movieApi) {
        return "tmdb".equalsIgnoreCase(movieApi.name());
    }

}
