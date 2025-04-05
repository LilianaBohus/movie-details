package hu.informula.movie_details.model.paging;

@FunctionalInterface
public interface PageFetcher<T> {
    PageResult<T> fetchPage(int page);
}

