package hu.informula.movie_details.model.omdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public record OmdbSearchResponse(
        @JsonProperty("Response") String response,
        @JsonProperty("Search") List<OmdbMovieDetailDto> search,
        @JsonProperty("totalResults") String totalResults
) implements Serializable {
}