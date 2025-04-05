package hu.informula.movie_details.model.enums;

public enum MovieApi {
    OMDB, TMDB;

    public static MovieApi from(String value) {
        try {
            return MovieApi.valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("Unsupported API: " + value);
        }
    }
}