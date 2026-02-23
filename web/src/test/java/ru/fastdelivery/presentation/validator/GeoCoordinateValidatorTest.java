package ru.fastdelivery.presentation.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.presentation.api.request.GeoPoint;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class GeoCoordinateValidatorTest {

    private final GeoCoordinateValidator validator = new GeoCoordinateValidator();

    @Test
    @DisplayName("null GeoPoint -> валиден")
    void whenNullGeoPoint_thenValid() {
        assertThat(validator.isValid(null, null)).isTrue();
    }

    @Test
    @DisplayName("Широта < 45 -> не валидна")
    void whenLatitudeBelow45_thenInvalid() {
        var geoPoint = new GeoPoint(new BigDecimal("44.999"), new BigDecimal("50"));
        assertThat(validator.isValid(geoPoint, null)).isFalse();
    }

    @Test
    @DisplayName("Широта > 65 -> не валидна")
    void whenLatitudeAbove65_thenInvalid() {
        var geoPoint = new GeoPoint(new BigDecimal("65.001"), new BigDecimal("50"));
        assertThat(validator.isValid(geoPoint, null)).isFalse();
    }

    @Test
    @DisplayName("Долгота < 30 -> не валидна")
    void whenLongitudeBelow30_thenInvalid() {
        var geoPoint = new GeoPoint(new BigDecimal("55"), new BigDecimal("29.999"));
        assertThat(validator.isValid(geoPoint, null)).isFalse();
    }

    @Test
    @DisplayName("Долгота > 96 -> не валидна")
    void whenLongitudeAbove96_thenInvalid() {
        var geoPoint = new GeoPoint(new BigDecimal("55"), new BigDecimal("96.001"));
        assertThat(validator.isValid(geoPoint, null)).isFalse();
    }

    @Test
    @DisplayName("Широта на границе 45 -> валидна")
    void whenLatitudeAtMinBoundary_thenValid() {
        var geoPoint = new GeoPoint(new BigDecimal("45"), new BigDecimal("50"));
        assertThat(validator.isValid(geoPoint, null)).isTrue();
    }

    @Test
    @DisplayName("Широта на границе 65 -> валидна")
    void whenLatitudeAtMaxBoundary_thenValid() {
        var geoPoint = new GeoPoint(new BigDecimal("65"), new BigDecimal("50"));
        assertThat(validator.isValid(geoPoint, null)).isTrue();
    }

    @Test
    @DisplayName("Долгота на границе 30 -> валидна")
    void whenLongitudeAtMinBoundary_thenValid() {
        var geoPoint = new GeoPoint(new BigDecimal("55"), new BigDecimal("30"));
        assertThat(validator.isValid(geoPoint, null)).isTrue();
    }

    @Test
    @DisplayName("Долгота на границе 96 -> валидна")
    void whenLongitudeAtMaxBoundary_thenValid() {
        var geoPoint = new GeoPoint(new BigDecimal("55"), new BigDecimal("96"));
        assertThat(validator.isValid(geoPoint, null)).isTrue();
    }

    @Test
    @DisplayName("Валидные координаты -> валидны")
    void whenValidCoordinates_thenValid() {
        var geoPoint = new GeoPoint(new BigDecimal("55.7558"), new BigDecimal("37.6173"));
        assertThat(validator.isValid(geoPoint, null)).isTrue();
    }
}
