package ru.fastdelivery.usecase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.dimensions.Dimensions;
import ru.fastdelivery.domain.common.dimensions.OverallSize;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

class TariffCalculateUseCaseTest {

  final WeightPriceProvider weightPriceProvider = mock(WeightPriceProvider.class);
  final VolumePriceProvider volumePriceProvider = mock(VolumePriceProvider.class);
  final Currency currency = new CurrencyFactory(code -> true).create("RUB");

  final TariffCalculateUseCase tariffCalculateUseCase =
      new TariffCalculateUseCase(weightPriceProvider, volumePriceProvider);

  @Test
  @DisplayName("Расчет стоимости доставки -> успешно")
  void whenCalculatePrice_thenSuccess() {
    Price minimalPrice = new Price(BigDecimal.TEN, currency);
    Price pricePerKg = new Price(BigDecimal.valueOf(100), currency);
    Price pricePerCubeMeter = new Price(BigDecimal.valueOf(200), currency);

    when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
    when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
    when(volumePriceProvider.costPerCubeMeter()).thenReturn(pricePerCubeMeter);

    var shipment =
        new Shipment(
            List.of(
                new Pack(
                    new Weight(BigInteger.valueOf(1200)),
                    new Dimensions(
                        new OverallSize(BigInteger.valueOf(500)),
                        new OverallSize(BigInteger.valueOf(600)),
                        new OverallSize(BigInteger.valueOf(700))))),
            new CurrencyFactory(code -> true).create("RUB"));
    var expectedPrice = new Price(BigDecimal.valueOf(120), currency);

    var actualPrice = tariffCalculateUseCase.calc(shipment);

    assertThat(actualPrice)
        .usingRecursiveComparison()
        .withComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
        .isEqualTo(expectedPrice);
  }

  @Test
  @DisplayName("Получение минимальной стоимости -> успешно")
  void whenMinimalPrice_thenSuccess() {
    BigDecimal minimalValue = BigDecimal.TEN;
    var minimalPrice = new Price(minimalValue, currency);
    when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);

    var actual = tariffCalculateUseCase.minimalPrice();

    assertThat(actual).isEqualTo(minimalPrice);
  }
}
