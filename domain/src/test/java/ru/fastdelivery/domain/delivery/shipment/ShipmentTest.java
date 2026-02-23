package ru.fastdelivery.domain.delivery.shipment;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.geo.GeoCoordinate;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.route.Route;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ShipmentTest {

    @Test
    @DisplayName("Суммирование веса всех упаковок")
    void whenSummarizingWeightOfAllPackages_thenReturnSum() {
        var weight1 = new Weight(BigInteger.TEN);
        var weight2 = new Weight(BigInteger.ONE);

        var packages = List.of(new Pack(weight1), new Pack(weight2));
        var shipment = new Shipment(packages, new CurrencyFactory(code -> true).create("RUB"));

        var massOfShipment = shipment.weightAllPackages();

        assertThat(massOfShipment.weightGrams()).isEqualByComparingTo(BigInteger.valueOf(11));
    }

    @Test
    @DisplayName("Создание отправления без маршрута -> route пустой")
    void whenCreateWithoutRoute_thenEmpty() {
        var packages = List.of(new Pack(new Weight(BigInteger.TEN)));
        var currency = new CurrencyFactory(code -> true).create("RUB");

        var shipment = new Shipment(packages, currency);

        assertThat(shipment.route()).isEmpty();
    }

    @Test
    @DisplayName("Создание отправления с маршрутом -> route заполнен")
    void whenCreateWithRoute_thenPresent() {
        var packages = List.of(new Pack(new Weight(BigInteger.TEN)));
        var currency = new CurrencyFactory(code -> true).create("RUB");
        var departure = new GeoCoordinate(new BigDecimal("55"), new BigDecimal("50"));
        var destination = new GeoCoordinate(new BigDecimal("60"), new BigDecimal("55"));
        var route = new Route(departure, destination);

        var shipment = new Shipment(packages, currency, Optional.of(route));

        assertThat(shipment.route()).isPresent();
        assertThat(shipment.route().get()).isEqualTo(route);
    }
}