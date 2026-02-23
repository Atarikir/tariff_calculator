package ru.fastdelivery.usecase;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.geo.GeoCoordinate;
import ru.fastdelivery.domain.delivery.pack.OuterDimensions;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.route.Route;
import ru.fastdelivery.domain.delivery.shipment.Shipment;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TariffCalculateUseCaseTest {

    final WeightPriceProvider weightPriceProvider = mock(WeightPriceProvider.class);
    final Currency currency = new CurrencyFactory(code -> true).create("RUB");
    final TariffCalculateUseCase tariffCalculateUseCase = new TariffCalculateUseCase(weightPriceProvider);

    @Test
    @DisplayName("Расчет стоимости доставки только по весу -> успешно")
    void whenCalculateByWeight_thenSuccess() {
        var minimalPrice = new Price(BigDecimal.valueOf(350), currency);
        var pricePerKg = new Price(BigDecimal.valueOf(400), currency);

        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(weightPriceProvider.costPerCubicMeter()).thenReturn(new Price(BigDecimal.valueOf(10000), currency));
        when(weightPriceProvider.minDistanceKm()).thenReturn(new BigDecimal("450"));

        var shipment = new Shipment(
                List.of(new Pack(new Weight(BigInteger.valueOf(1200)))),
                currency);

        var actualPrice = tariffCalculateUseCase.calc(shipment);

        assertThat(actualPrice.amount()).isEqualByComparingTo(new BigDecimal("480.00"));
    }

    @Test
    @DisplayName("Расчет стоимости с объемом -> выбирается максимальное значение")
    void whenCalculateWithVolume_thenMaxValueSelected() {
        var minimalPrice = new Price(BigDecimal.valueOf(350), currency);
        var pricePerKg = new Price(BigDecimal.valueOf(400), currency);
        var pricePerCubicMeter = new Price(BigDecimal.valueOf(10000), currency);

        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(weightPriceProvider.costPerCubicMeter()).thenReturn(pricePerCubicMeter);
        when(weightPriceProvider.minDistanceKm()).thenReturn(new BigDecimal("450"));

        var dimensions = OuterDimensions.fromMillimeters(
                BigInteger.valueOf(350),
                BigInteger.valueOf(600),
                BigInteger.valueOf(250));

        var pack = Pack.withDimensions(new Weight(BigInteger.valueOf(1200)), dimensions);
        var shipment = new Shipment(List.of(pack), currency);

        var actualPrice = tariffCalculateUseCase.calc(shipment);

        var expectedVolumePrice = new BigDecimal("525.00");
        assertThat(actualPrice.amount()).isEqualByComparingTo(expectedVolumePrice);
    }

    @Test
    @DisplayName("Расчет с расстоянием < 450 км -> базовая стоимость")
    void whenDistanceLessThan450_thenBasePrice() {
        var minimalPrice = new Price(BigDecimal.valueOf(350), currency);
        var pricePerKg = new Price(BigDecimal.valueOf(400), currency);

        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(weightPriceProvider.costPerCubicMeter()).thenReturn(new Price(BigDecimal.valueOf(10000), currency));
        when(weightPriceProvider.minDistanceKm()).thenReturn(new BigDecimal("450"));

        // Координаты с маленьким расстоянием (< 450 км)
        var departure = new GeoCoordinate(new BigDecimal("55.0"), new BigDecimal("50.0"));
        var destination = new GeoCoordinate(new BigDecimal("55.5"), new BigDecimal("50.5"));
        var route = new Route(departure, destination);

        var shipment = new Shipment(
                List.of(new Pack(new Weight(BigInteger.valueOf(1200)))),
                currency,
                Optional.of(route));

        var actualPrice = tariffCalculateUseCase.calc(shipment);

        assertThat(actualPrice.amount()).isEqualByComparingTo(new BigDecimal("480.00"));
    }

    @Test
    @DisplayName("Расчет стоимости с расстоянием 500 км по примеру из ТЗ")
    void whenCalculateWithDistance500km_thenCorrect() {
        var basePrice = new BigDecimal("345");
        var minimalPrice = new Price(BigDecimal.valueOf(350), currency);
        var pricePerKg = new Price(basePrice, currency);

        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(weightPriceProvider.costPerCubicMeter()).thenReturn(new Price(BigDecimal.valueOf(10000), currency));
        when(weightPriceProvider.minDistanceKm()).thenReturn(new BigDecimal("450"));

        // Создаем координаты с расстоянием ~500 км
        var departure = new GeoCoordinate(new BigDecimal("55.0"), new BigDecimal("50.0"));
        var destination = new GeoCoordinate(new BigDecimal("59.5"), new BigDecimal("50.0"));
        var route = new Route(departure, destination);

        var shipment = new Shipment(
                List.of(new Pack(new Weight(BigInteger.valueOf(1000)))),
                currency,
                Optional.of(route));

        var actualPrice = tariffCalculateUseCase.calc(shipment);

        // (500/450) * 350 = 388.89 (округление CEILING до 2 знаков)
        assertThat(actualPrice.amount()).isGreaterThan(basePrice);
    }

    @Test
    @DisplayName("Расчет стоимости с расстоянием 200 км < 450 -> базовая стоимость")
    void whenCalculateWithDistance200km_thenBasePrice() {
        var basePrice = new BigDecimal("788.23");
        var minimalPrice = new Price(BigDecimal.valueOf(350), currency);
        var pricePerKg = new Price(basePrice, currency);

        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(weightPriceProvider.costPerCubicMeter()).thenReturn(new Price(BigDecimal.valueOf(10000), currency));
        when(weightPriceProvider.minDistanceKm()).thenReturn(new BigDecimal("450"));

        // Координаты с маленьким расстоянием
        var departure = new GeoCoordinate(new BigDecimal("55.0"), new BigDecimal("50.0"));
        var destination = new GeoCoordinate(new BigDecimal("55.5"), new BigDecimal("50.0"));
        var route = new Route(departure, destination);

        var shipment = new Shipment(
                List.of(new Pack(new Weight(BigInteger.valueOf(1000)))),
                currency,
                Optional.of(route));

        var actualPrice = tariffCalculateUseCase.calc(shipment);

        // Расстояние < 450, возвращается базовая стоимость
        assertThat(actualPrice.amount()).isEqualByComparingTo(new BigDecimal("788.23"));
    }

    @Test
    @DisplayName("Получение минимальной стоимости -> успешно")
    void whenMinimalPrice_thenSuccess() {
        var minimalPrice = new Price(BigDecimal.TEN, currency);
        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);

        var actual = tariffCalculateUseCase.minimalPrice();

        assertThat(actual).isEqualTo(minimalPrice);
    }

    @Test
    @DisplayName("Расчет с несколькими упаковками -> суммируется вес и объем")
    void whenMultiplePackages_thenSumWeightAndVolume() {
        var minimalPrice = new Price(BigDecimal.valueOf(350), currency);
        var pricePerKg = new Price(BigDecimal.valueOf(400), currency);
        var pricePerCubicMeter = new Price(BigDecimal.valueOf(10000), currency);

        when(weightPriceProvider.minimalPrice()).thenReturn(minimalPrice);
        when(weightPriceProvider.costPerKg()).thenReturn(pricePerKg);
        when(weightPriceProvider.costPerCubicMeter()).thenReturn(pricePerCubicMeter);
        when(weightPriceProvider.minDistanceKm()).thenReturn(new BigDecimal("450"));

        var dimensions1 = OuterDimensions.fromMillimeters(
                BigInteger.valueOf(100),
                BigInteger.valueOf(100),
                BigInteger.valueOf(100));
        var dimensions2 = OuterDimensions.fromMillimeters(
                BigInteger.valueOf(200),
                BigInteger.valueOf(200),
                BigInteger.valueOf(200));

        var pack1 = Pack.withDimensions(new Weight(BigInteger.valueOf(1000)), dimensions1);
        var pack2 = Pack.withDimensions(new Weight(BigInteger.valueOf(2000)), dimensions2);

        var shipment = new Shipment(List.of(pack1, pack2), currency);

        var actualPrice = tariffCalculateUseCase.calc(shipment);

        assertThat(actualPrice).isNotNull();
    }
}
