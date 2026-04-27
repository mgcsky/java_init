package com.example.demo.fare.domain;

import com.example.demo.fare.application.FareServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class FareCalculator {

    private static final Logger log = LoggerFactory.getLogger(FareCalculator.class);

    private static final BigDecimal DISTANCE_RATE = new BigDecimal("2500");
    private static final BigDecimal WAITING_RATE = new BigDecimal("500");
    private static final BigDecimal TAX_RATE = new BigDecimal("0.10");

    public FareResult calculate(BigDecimal baseFare, int distanceKm, int waitingMinutes, BigDecimal surcharge) {
        log.debug("Input: baseFare={}, distanceKm={}, waitingMinutes={}, surcharge={}",
                baseFare, distanceKm, waitingMinutes, surcharge);

        BigDecimal distanceFee = DISTANCE_RATE.multiply(BigDecimal.valueOf(distanceKm));
        BigDecimal waitingFee = WAITING_RATE.multiply(BigDecimal.valueOf(waitingMinutes));

        BigDecimal subTotal = baseFare.add(distanceFee).add(waitingFee).add(surcharge);
        BigDecimal tax = TAX_RATE.multiply(subTotal);
        BigDecimal total = subTotal.add(tax);

        log.debug("Computed: distanceFee={}, waitingFee={}, subTotal={}, tax={}, total={}",
                distanceFee, waitingFee, subTotal, tax, total);

        return new FareResult(
                subTotal.setScale(0, RoundingMode.HALF_UP),
                tax.setScale(0, RoundingMode.HALF_UP),
                total.setScale(0, RoundingMode.HALF_UP)
        );
    }

    public record FareResult(BigDecimal subtotal, BigDecimal tax, BigDecimal total) {}
}
