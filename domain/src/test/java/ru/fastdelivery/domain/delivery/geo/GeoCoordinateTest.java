package ru.fastdelivery.domain.delivery.geo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class GeoCoordinateTest {

    @Test
    @DisplayName("Широта меньше 45 -> исключение")
    void whenLatitudeBelow45_thenException() {
        assertThatThrownBy(() -> new GeoCoordinate(
                        new BigDecimal("44.999"),
                        new BigDecimal("50")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Широта больше 65 -> исключение")
    void whenLatitudeAbove65_thenException() {
        assertThatThrownBy(() -> new GeoCoordinate(
                        new BigDecimal("65.001"),
                        new BigDecimal("50")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Долгота меньше 30 -> исключение")
    void whenLongitudeBelow30_thenException() {
        assertThatThrownBy(() -> new GeoCoordinate(
                        new BigDecimal("55"),
                        new BigDecimal("29.999")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Долгота больше 96 -> исключение")
    void whenLongitudeAbove96_thenException() {
        assertThatThrownBy(() -> new GeoCoordinate(
                        new BigDecimal("55"),
                        new BigDecimal("96.001")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Широта 45 (граница) -> успешно")
    void whenLatitudeAtMinBoundary_thenCreated() {
        var coord = new GeoCoordinate(new BigDecimal("45"), new BigDecimal("50"));
        assertThat(coord.latitude()).isEqualByComparingTo(new BigDecimal("45"));
    }

    @Test
    @DisplayName("Широта 65 (граница) -> успешно")
    void whenLatitudeAtMaxBoundary_thenCreated() {
        var coord = new GeoCoordinate(new BigDecimal("65"), new BigDecimal("50"));
        assertThat(coord.latitude()).isEqualByComparingTo(new BigDecimal("65"));
    }

    @Test
    @DisplayName("Долгота 30 (граница) -> успешно")
    void whenLongitudeAtMinBoundary_thenCreated() {
        var coord = new GeoCoordinate(new BigDecimal("55"), new BigDecimal("30"));
        assertThat(coord.longitude()).isEqualByComparingTo(new BigDecimal("30"));
    }

    @Test
    @DisplayName("Долгота 96 (граница) -> успешно")
    void whenLongitudeAtMaxBoundary_thenCreated() {
        var coord = new GeoCoordinate(new BigDecimal("55"), new BigDecimal("96"));
        assertThat(coord.longitude()).isEqualByComparingTo(new BigDecimal("96"));
    }

    @Test
    @DisplayName("Валидные координаты -> успешно")
    void whenValidCoordinates_thenCreated() {
        var coord = new GeoCoordinate(new BigDecimal("55.7558"), new BigDecimal("37.6173"));
        assertThat(coord.latitude()).isEqualByComparingTo(new BigDecimal("55.7558"));
        assertThat(coord.longitude()).isEqualByComparingTo(new BigDecimal("37.6173"));
    }
}
