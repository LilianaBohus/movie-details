package hu.informula.movie_details.util.parsing;

import hu.informula.movie_details.model.tmdb.TmdbCreditsResponse;
import hu.informula.movie_details.model.tmdb.TmdbCrewMember;
import hu.informula.movie_details.util.ParseUtil;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ParseUtilTest {

    @Test
    void testCalculateTotalPages_withValidNumber() {
        assertEquals(3, ParseUtil.calculateTotalPages("25"));
        assertEquals(1, ParseUtil.calculateTotalPages("10"));
        assertEquals(2, ParseUtil.calculateTotalPages("11"));
    }

    @Test
    void testCalculateTotalPages_withNull() {
        assertEquals(1, ParseUtil.calculateTotalPages(null));
    }

    @Test
    void testCalculateTotalPages_withInvalidNumber() {
        assertEquals(1, ParseUtil.calculateTotalPages("abc"));
    }

    @Test
    void testSplitDirectors_withValidString() {
        String raw = "John Doe, Jane Smith, Alice";
        List<String> expected = List.of("John Doe", "Jane Smith", "Alice");
        assertEquals(expected, ParseUtil.splitDirectors(raw));
    }

    @Test
    void testSplitDirectors_withNull() {
        assertTrue(ParseUtil.splitDirectors(null).isEmpty());
    }

    @Test
    void testSplitDirectors_withBlank() {
        assertTrue(ParseUtil.splitDirectors("   ").isEmpty());
    }

    @Test
    void testExtractYearFromDate_withValidDate() {
        assertEquals("2024", ParseUtil.extractYearFromDate("2024-05-01"));
    }

    @Test
    void testExtractYearFromDate_withEmptyOrNull() {
        assertEquals("", ParseUtil.extractYearFromDate(""));
        assertEquals("", ParseUtil.extractYearFromDate(null));
    }

    @Test
    void testExtractDirectorsFromTmdbCredits() {
        var crew = List.of(
                new TmdbCrewMember("Director", "Greta Gerwig"),
                new TmdbCrewMember("Producer", "Emma Thomas"),
                new TmdbCrewMember("Director", "Christopher Nolan")
        );

        var credits = new TmdbCreditsResponse(crew);

        var result = ParseUtil.extractDirectorsFromTmdbCredits(credits);

        assertEquals(List.of("Greta Gerwig", "Christopher Nolan"), result);
    }
}