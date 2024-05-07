package ru.fastdelivery.usecase;

import java.math.BigDecimal;
import javax.inject.Named;
import lombok.RequiredArgsConstructor;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

@Named
@RequiredArgsConstructor
public class TariffCalculateUseCase {
  private final WeightPriceProvider weightPriceProvider;
  private final VolumePriceProvider volumePriceProvider;

  public Price calc(Shipment shipment) {
    BigDecimal weightAllPackagesKg = shipment.weightAllPackages().kilograms();
    BigDecimal volumeAllPackages = shipment.volumeAllPackages();
    Price minimalPrice = weightPriceProvider.minimalPrice();

    Price priceVolumeAllPackages =
        volumePriceProvider.costPerCubeMeter().multiply(volumeAllPackages);
    Price priceWeightAllPackages =
        weightPriceProvider.costPerKg().multiply(weightAllPackagesKg).max(minimalPrice);

    return priceVolumeAllPackages.amount().compareTo(priceWeightAllPackages.amount()) > 0
        ? priceVolumeAllPackages
        : priceWeightAllPackages;
  }

  public Price minimalPrice() {
    return weightPriceProvider.minimalPrice();
  }
}
