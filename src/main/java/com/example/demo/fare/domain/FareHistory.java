package com.example.demo.fare.domain;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "fare_histories"
)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FareHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "fare", nullable = false)
    private BigDecimal fare;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public FareHistory(Long userId, BigDecimal fare) {
        this.userId = userId;
        this.fare = fare;
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}
