package hu.informula.movie_details.service;

import hu.informula.movie_details.model.MovieResponse;

public interface MovieService {
   MovieResponse getMoviesByTitle(String title);
}
