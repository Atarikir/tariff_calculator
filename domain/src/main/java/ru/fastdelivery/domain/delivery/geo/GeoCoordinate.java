package ru.fastdelivery.domain.delivery.geo;

import ru.fastdelivery.domain.exception.InvalidGeoCoordinateException;

import java.math.BigDecimal;

/**
 * Географическая координата
 *
 * @param latitude  широта (от 45 до 65)
 * @param longitude долгота (от 30 до 96)
 */
public record GeoCoordinate(
        BigDecimal latitude,
        BigDecimal longitude
) {

    private static final BigDecimal MIN_LATITUDE = new BigDecimal("45");
    private static final BigDecimal MAX_LATITUDE = new BigDecimal("65");
    private static final BigDecimal MIN_LONGITUDE = new BigDecimal("30");
    private static final BigDecimal MAX_LONGITUDE = new BigDecimal("96");

    public GeoCoordinate {
        validateLatitude(latitude);
        validateLongitude(longitude);
    }

    private static void validateLatitude(BigDecimal latitude) {
        if (latitude.compareTo(MIN_LATITUDE) < 0 || latitude.compareTo(MAX_LATITUDE) > 0) {
            throw InvalidGeoCoordinateException.invalidLatitude(
                    latitude.doubleValue(), 45, 65);
        }
    }

    private static void validateLongitude(BigDecimal longitude) {
        if (longitude.compareTo(MIN_LONGITUDE) < 0 || longitude.compareTo(MAX_LONGITUDE) > 0) {
            throw InvalidGeoCoordinateException.invalidLongitude(
                    longitude.doubleValue(), 30, 96);
        }
    }
}
