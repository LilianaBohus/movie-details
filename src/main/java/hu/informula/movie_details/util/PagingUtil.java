package hu.informula.movie_details.util;

import hu.informula.movie_details.model.paging.PageFetcher;
import hu.informula.movie_details.model.paging.PageResult;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class PagingUtil {

    public static <T> List<T> fetchAllParallel(PageFetcher<T> fetcher) {
        PageResult<T> firstPage = fetcher.fetchPage(1);
        int totalPages = firstPage.totalPages();

        if (totalPages <= 1) {
            return new ArrayList<>(firstPage.items());
        }

        List<T> results = new ArrayList<>(firstPage.items());

        results.addAll(
                IntStream.rangeClosed(2, totalPages)
                        .parallel()
                        .mapToObj(fetcher::fetchPage)
                        .flatMap(page -> page.items().stream())
                        .toList()
        );

        return results;
    }
}