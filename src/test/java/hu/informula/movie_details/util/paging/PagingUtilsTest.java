package hu.informula.movie_details.util.paging;

import hu.informula.movie_details.model.paging.PageFetcher;
import hu.informula.movie_details.model.paging.PageResult;
import hu.informula.movie_details.util.PagingUtil;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class PagingUtilsTest {

    @Test
    void testFetchAllParallel_withSinglePage() {
        PageFetcher<String> fetcher = page -> new PageResult<>(List.of("A", "B", "C"), 1);

        List<String> result = PagingUtil.fetchAllParallel(fetcher);

        assertEquals(List.of("A", "B", "C"), result);
    }

    @Test
    void testFetchAllParallel_withMultiplePages() {
        PageFetcher<String> fetcher = page -> switch (page) {
            case 1 -> new PageResult<>(List.of("A", "B"), 3);
            case 2 -> new PageResult<>(List.of("C", "D"), 3);
            case 3 -> new PageResult<>(List.of("E"), 3);
            default -> throw new IllegalArgumentException("Unexpected page: " + page);
        };

        List<String> result = PagingUtil.fetchAllParallel(fetcher);

        assertTrue(result.containsAll(List.of("A", "B", "C", "D", "E")));
        assertEquals(5, result.size());
    }

    @Test
    void testFetchAllParallel_preservesItemCount() {
        int pages = 5;
        int itemsPerPage = 3;

        PageFetcher<Integer> fetcher = page -> {
            List<Integer> items = List.of(
                    (page - 1) * itemsPerPage + 1,
                    (page - 1) * itemsPerPage + 2,
                    (page - 1) * itemsPerPage + 3
            );
            return new PageResult<>(items, pages);
        };

        List<Integer> result = PagingUtil.fetchAllParallel(fetcher);

        assertEquals(pages * itemsPerPage, result.size());
        assertTrue(result.contains(1));
        assertTrue(result.contains(15));
    }

    @Test
    void testFetchAllParallel_parallelExecutionSafety() {
        AtomicInteger callCounter = new AtomicInteger(0);

        PageFetcher<String> fetcher = page -> {
            callCounter.incrementAndGet();
            return switch (page) {
                case 1 -> new PageResult<>(List.of("Start"), 3);
                case 2 -> new PageResult<>(List.of("Middle"), 3);
                case 3 -> new PageResult<>(List.of("End"), 3);
                default -> new PageResult<>(List.of(), 3);
            };
        };

        List<String> result = PagingUtil.fetchAllParallel(fetcher);

        assertEquals(3, callCounter.get());
        assertTrue(result.containsAll(List.of("Start", "Middle", "End")));
    }

}