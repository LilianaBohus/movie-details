package hu.informula.movie_details.model.paging;

import java.util.List;

public record PageResult<T>(
        List<T> items,
        int totalPages
) {}
