package hu.informula.movie_details.util;

import hu.informula.movie_details.model.tmdb.TmdbCreditsResponse;
import hu.informula.movie_details.model.tmdb.TmdbCrewMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ParseUtil {
    private static final Logger log = LoggerFactory.getLogger(ParseUtil.class);
    private static final double OMDB_RESULTS_PER_PAGE = 10.0;

    public static int calculateTotalPages(String totalResultsStr) {
        if (totalResultsStr == null) return 1;
        try {
            int totalResults = Integer.parseInt(totalResultsStr);
            return (int) Math.ceil(totalResults / OMDB_RESULTS_PER_PAGE);
        } catch (NumberFormatException e) {
            log.warn("Invalid totalResults value: {}", totalResultsStr);
            return 1;
        }
    }

    public static List<String> splitDirectors(String rawDirectors) {
        if (rawDirectors == null || rawDirectors.isBlank()) return List.of();
        return Arrays.stream(rawDirectors.split(",\\s*")).toList();
    }

    public static List<String> extractDirectorsFromTmdbCredits(TmdbCreditsResponse credits) {
        return credits.crew().stream()
                .filter(crew -> "Director".equalsIgnoreCase(crew.job()))
                .map(TmdbCrewMember::name)
                .toList();
    }

    // extracts the year from a full date string (like "2024-07-18") assuming the format of "YYYY-MM-DD"
    public static String extractYearFromDate(String releaseDate) {
        if (releaseDate != null && !releaseDate.isBlank()) {
            return releaseDate.split("-")[0];
        }
        return "";
    }
}
