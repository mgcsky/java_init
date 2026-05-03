package com.example.demo.fare.infrastructure;

import com.example.demo.fare.domain.FareHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;

public interface FareHistoryRepository extends JpaRepository<FareHistory, Long>, JpaSpecificationExecutor<FareHistory> {

    Page<FareHistory> findAll(
            Pageable pageable
    );

    Page<FareHistory> findByFareGreaterThanEqual(
            BigDecimal minFare,
            Pageable pageable
    );

    Page<FareHistory> findByUserId(
            Long userId,
            Pageable pageable
    );

    Page<FareHistory> findByUserIdAndFareGreaterThanEqual(
            Long userId,
            BigDecimal minFare,
            Pageable pageable
    );
}
