package com.example.demo.fare.application;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class FareCalculationCommand {
    private BigDecimal baseFare;

    private int distanceKm;

    private int waitingMinutes;

    private BigDecimal surcharge;

    public FareCalculationCommand(
            BigDecimal baseFare,
            int distanceKm,
            int waitingMinutes,
            BigDecimal surcharge
    ) {
        this.baseFare = baseFare;
        this.distanceKm = distanceKm;
        this.waitingMinutes = waitingMinutes;
        this.surcharge = surcharge;
    }
}
