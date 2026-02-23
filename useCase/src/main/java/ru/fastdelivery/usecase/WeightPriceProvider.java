package ru.fastdelivery.usecase;

import ru.fastdelivery.domain.common.price.Price;

import java.math.BigDecimal;

public interface WeightPriceProvider {
    Price costPerKg();

    Price minimalPrice();

    Price costPerCubicMeter();

    BigDecimal minDistanceKm();
}
