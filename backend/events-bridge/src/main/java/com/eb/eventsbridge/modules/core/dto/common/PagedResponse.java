package com.eb.eventsbridge.modules.core.dto.common;


import java.util.List;

public record PagedResponse<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages
) {}
