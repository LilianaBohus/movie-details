package hu.informula.movie_details.model.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbSearchResult(
        @JsonProperty("id") int id
) {}
