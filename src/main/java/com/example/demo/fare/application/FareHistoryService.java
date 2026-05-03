package com.example.demo.fare.application;

import com.example.demo.fare.api.v1.dto.FareHistoryCreatingRequest;
import com.example.demo.fare.api.v1.dto.FareHistoryCreatingResponse;
import com.example.demo.fare.api.v1.dto.FareHistoryItemResponse;
import com.example.demo.fare.api.v1.dto.FareHistoryListResponse;
import com.example.demo.fare.domain.FareHistory;
import com.example.demo.fare.infrastructure.FareHistoryRepository;
import com.example.demo.fare.infrastructure.FareHistorySpecifications;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class FareHistoryService {

    private final FareHistoryRepository fareHistoryRepository;

    public FareHistoryService (FareHistoryRepository fareHistoryRepository) {
        this.fareHistoryRepository = fareHistoryRepository;
    }

    public FareHistoryCreatingResponse insert(FareHistoryCreatingRequest request) {

        FareHistory fareHistory = new FareHistory(
                request.userId(), request.fare()
        );

        FareHistory savedHistory = fareHistoryRepository.saveAndFlush(fareHistory);

        return new FareHistoryCreatingResponse(
                savedHistory.getId(),
                savedHistory.getFare(),
                savedHistory.getUserId(),
                savedHistory.getCreatedAt()
        );
    }

    public FareHistoryListResponse getHistoriesWithCollection(Long userId, BigDecimal minFare, String sort, int page, int size){
        List<FareHistory> fareHistoryList = fareHistoryRepository.findAll();

        Comparator<FareHistory> comparator = Comparator.comparing(FareHistory::getCreatedAt);

        if ("fare,asc".equalsIgnoreCase(sort))
            comparator = Comparator.comparing(FareHistory::getFare);

        if ("fare,desc".equalsIgnoreCase(sort))
            comparator = Comparator.comparing(FareHistory::getFare).reversed();

        if ("createdAt,desc".equalsIgnoreCase(sort))
            comparator = Comparator.comparing(FareHistory::getCreatedAt).reversed();


        List<FareHistoryItemResponse> filtered = fareHistoryList.stream()
                .filter(x -> userId == null || x.getUserId().equals(userId))
                .filter(x -> minFare == null || x.getFare().compareTo(minFare) >= 0)
                .sorted(comparator)
                .map(x -> new FareHistoryItemResponse(x.getId(), x.getUserId(), x.getFare(), x.getCreatedAt()))
                .toList();

        long total = filtered.size();
        int from = Math.min(page * size, filtered.size());
        int to = Math.min(from + size, filtered.size());
        List<FareHistoryItemResponse> paged = filtered.subList(from, to);

        return new FareHistoryListResponse(paged, page, size, total);
    }

    public FareHistoryListResponse getHistories(Long userId, BigDecimal minFare, String sort, int page, int size){
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<FareHistory> p;
        if (userId != null && minFare != null) {
            p = fareHistoryRepository.findByUserIdAndFareGreaterThanEqual(
                    userId, minFare, pageable
            );
        }
        else if (userId != null) {
             p = fareHistoryRepository.findByUserId(
                    userId, pageable
             );
        } else if (minFare != null) {
             p = fareHistoryRepository.findByFareGreaterThanEqual(
               minFare, pageable
             );
        } else {
            p = fareHistoryRepository.findAll(pageable);
        }

        var items = p.getContent().stream()
                .map(h -> new FareHistoryItemResponse(h.getId(), h.getUserId(), h.getFare(), h.getCreatedAt()))
                .toList();

        return new FareHistoryListResponse(
                items, page, size, p.getTotalElements()
        );
    }

    public FareHistoryListResponse getHistoriesWithSpecification(Long userId, BigDecimal minFare, String sort, int page, int size) {
        Specification<FareHistory> spec = FareHistorySpecifications.forList(userId, minFare);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<FareHistory> p = fareHistoryRepository.findAll(spec, pageable);

        var item = p.getContent().stream()
                .map(h -> new FareHistoryItemResponse(h.getId(), h.getUserId(), h.getFare(), h.getCreatedAt()))
                .toList();

        return new FareHistoryListResponse(item, page, size, p.getTotalElements());
    }
}
