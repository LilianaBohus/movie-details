package hu.informula.movie_details.model.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbCrewMember(
        @JsonProperty("job") String job,
        @JsonProperty("name") String name
) {}

