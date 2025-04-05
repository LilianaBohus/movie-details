package hu.informula.movie_details.model.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public record TmdbSearchResponse(
        @JsonProperty("results") List<TmdbSearchResult> results,
        @JsonProperty("total_pages") int totalPages
) {}