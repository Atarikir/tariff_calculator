package ru.fastdelivery.domain.delivery.route;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.delivery.geo.GeoCoordinate;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RouteTest {

    @Test
    @DisplayName("Создание маршрута с null departure -> исключение")
    void whenNullDeparture_thenException() {
        var destination = new GeoCoordinate(new BigDecimal("55"), new BigDecimal("50"));

        assertThatThrownBy(() -> new Route(null, destination))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Создание маршрута с null destination -> исключение")
    void whenNullDestination_thenException() {
        var departure = new GeoCoordinate(new BigDecimal("55"), new BigDecimal("50"));

        assertThatThrownBy(() -> new Route(departure, null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Создание маршрута с валидными координатами -> успешно")
    void whenValidCoordinates_thenCreated() {
        var departure = new GeoCoordinate(new BigDecimal("55.7558"), new BigDecimal("37.6173"));
        var destination = new GeoCoordinate(new BigDecimal("59.9343"), new BigDecimal("30.3351"));

        var route = new Route(departure, destination);

        assertThat(route.departure()).isEqualTo(departure);
        assertThat(route.destination()).isEqualTo(destination);
    }
}
