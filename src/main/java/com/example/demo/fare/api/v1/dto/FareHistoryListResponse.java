package com.example.demo.fare.api.v1.dto;

import java.util.List;

public record FareHistoryListResponse(
        List<FareHistoryItemResponse> items,
        int page,
        int size,
        long total
) {
}
