package hu.informula.movie_details.service;

import hu.informula.movie_details.model.MovieResponse;
import hu.informula.movie_details.model.enums.MovieApi;

public interface MovieService {
   MovieResponse getMoviesByTitle(String title);
   boolean canHandle(MovieApi movieApi);
}
