package ru.fastdelivery.domain.exception;

/**
 * Исключение при некорректных географических координатах
 */
public class InvalidGeoCoordinateException extends DomainException {
    public InvalidGeoCoordinateException(String message) {
        super(message);
    }

    public static InvalidGeoCoordinateException invalidLatitude(double value, double min, double max) {
        return new InvalidGeoCoordinateException(
                String.format("Latitude %.4f is out of range [%.0f, %.0f]", value, min, max));
    }

    public static InvalidGeoCoordinateException invalidLongitude(double value, double min, double max) {
        return new InvalidGeoCoordinateException(
                String.format("Longitude %.4f is out of range [%.0f, %.0f]", value, min, max));
    }
}
