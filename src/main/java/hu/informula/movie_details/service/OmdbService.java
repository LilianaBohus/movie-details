package hu.informula.movie_details.service;

import hu.informula.movie_details.client.OmdbClient;
import hu.informula.movie_details.model.MovieDto;
import hu.informula.movie_details.model.MovieResponse;
import hu.informula.movie_details.model.omdb.OmdbMovieDetailDto;
import hu.informula.movie_details.model.paging.PageResult;
import hu.informula.movie_details.util.PagingUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static hu.informula.movie_details.util.ParseUtil.calculateTotalPages;
import static hu.informula.movie_details.util.ParseUtil.splitDirectors;

@Service
public class OmdbService implements MovieService {
    private static final Logger log = LoggerFactory.getLogger(OmdbService.class);

    private final OmdbClient omdbClient;
    private final Executor executor = Executors.newFixedThreadPool(10); // or inject TaskExecutor todo

    public OmdbService(OmdbClient omdbClient) {
        this.omdbClient = omdbClient;
    }

    @Override
    public MovieResponse getMoviesByTitle(String title) {
        var searchResults = PagingUtil.fetchAllParallel(page -> {
            var response = omdbClient.searchByTitle(title, page);
            return new PageResult<>(
                    response.search(),
                    calculateTotalPages(response.totalResults())
            );
        });

        var futures = searchResults.stream()
                .map(result -> CompletableFuture.supplyAsync(() ->
                        (toMovieDto(result)), executor))
                .toList();

        var movieDtos = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        return new MovieResponse(movieDtos);
    }

    private MovieDto toMovieDto(OmdbMovieDetailDto result) {
        var detail = omdbClient.getDetailsByImdbId(result.imdbID());
        var directors = splitDirectors(detail.director());
        return new MovieDto(detail.title(), detail.year(), directors);
    }

}
