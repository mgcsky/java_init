package com.example.demo.fare.infrastructure;

import com.example.demo.fare.domain.FareHistory;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class FareHistorySpecifications {
    private FareHistorySpecifications() {
    }

    public static Specification<FareHistory> userIdEqual(Long userId) {
        if (userId == null) {
            return (root, query, cb) -> cb.conjunction();
        }

        return (root, query, cb) -> cb.equal(root.get("userId"), userId);
    }

    public static Specification<FareHistory> fareAtLeast(BigDecimal minFare) {
        if (minFare == null) {
            return (root, query, cb) -> cb.conjunction();
        }

        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("fare"), minFare);
    }

    public static Specification<FareHistory> forList(Long userId, BigDecimal minFare) {
        return Specification.where(userIdEqual(userId)).and(fareAtLeast(minFare));
    }
}
