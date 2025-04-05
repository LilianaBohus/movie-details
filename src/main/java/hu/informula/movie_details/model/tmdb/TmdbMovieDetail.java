package hu.informula.movie_details.model.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbMovieDetail(
        @JsonProperty("title") String title,
        @JsonProperty("release_date") String releaseDate,
        @JsonProperty("id") int id
) {}
