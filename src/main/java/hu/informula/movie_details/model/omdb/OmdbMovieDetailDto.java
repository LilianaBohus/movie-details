package hu.informula.movie_details.model.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record OmdbMovieDetailDto(
        @JsonProperty("Title") String title,
        @JsonProperty("Year") String year,
        @JsonProperty("imdbID") String imdbID,
        @JsonProperty("Type") String type,
        @JsonProperty("Poster") String poster,
        @JsonProperty("Director") String director
) implements Serializable {
}
