package ru.fastdelivery.usecase;

import lombok.RequiredArgsConstructor;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.geo.Distance;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.pack.Volume;
import ru.fastdelivery.domain.delivery.route.Route;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import javax.inject.Named;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Named
@RequiredArgsConstructor
public class TariffCalculateUseCase {
    private static final BigDecimal KM_DIVISOR = new BigDecimal("450");

    private final WeightPriceProvider weightPriceProvider;

    public Price calc(Shipment shipment) {
        Price weightBasedPrice = calculateWeightBasedPrice(shipment);
        Price volumeBasedPrice = calculateVolumeBasedPrice(shipment);

        Price basePrice = weightBasedPrice.max(volumeBasedPrice);

        return shipment.route()
                .map(route -> calculateDistanceBasedPrice(basePrice, route))
                .orElse(basePrice);
    }

    private Price calculateWeightBasedPrice(Shipment shipment) {
        var weightAllPackagesKg = shipment.weightAllPackages().kilograms();
        var minimalPrice = weightPriceProvider.minimalPrice();

        return weightPriceProvider
                .costPerKg()
                .multiply(weightAllPackagesKg)
                .max(minimalPrice);
    }

    private Price calculateVolumeBasedPrice(Shipment shipment) {
        var totalVolume = calculateTotalVolume(shipment);
        var minimalPrice = weightPriceProvider.minimalPrice();

        var volumePrice = weightPriceProvider.costPerCubicMeter().multiply(totalVolume);

        return volumePrice.max(minimalPrice);
    }

    private BigDecimal calculateTotalVolume(Shipment shipment) {
        return shipment.packages().stream()
                .map(Pack::dimensions)
                .filter(java.util.Optional::isPresent)
                .map(java.util.Optional::get)
                .map(Volume::fromDimensions)
                .map(Volume::cubicMeters)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Price calculateDistanceBasedPrice(Price basePrice, Route route) {
        Distance distance = Distance.between(route.departure(), route.destination());
        BigDecimal minDistance = weightPriceProvider.minDistanceKm();

        if (distance.kilometers().compareTo(minDistance) < 0) {
            return basePrice;
        }

        BigDecimal multiplier = distance.kilometers()
                .divide(minDistance, 10, RoundingMode.HALF_UP);

        BigDecimal resultAmount = basePrice.amount()
                .multiply(multiplier)
                .setScale(2, RoundingMode.CEILING);

        return new Price(resultAmount, basePrice.currency());
    }

    public Price minimalPrice() {
        return weightPriceProvider.minimalPrice();
    }
}
