package hu.informula.movie_details.model.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TmdbCreditsResponse(
        @JsonProperty("crew") List<TmdbCrewMember> crew
) {}