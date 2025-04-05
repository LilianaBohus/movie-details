package hu.informula.movie_details.model;

import java.util.List;

// Unified format to return to the user
public record MovieDto(
        String title,
        String year,
        List<String> director
) {
}
