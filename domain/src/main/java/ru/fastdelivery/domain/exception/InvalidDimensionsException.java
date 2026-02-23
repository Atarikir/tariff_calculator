package ru.fastdelivery.domain.exception;

/**
 * Исключение при некорректных габаритах
 */
public class InvalidDimensionsException extends DomainException {
    public InvalidDimensionsException(String message) {
        super(message);
    }

    public static InvalidDimensionsException negative(String dimension) {
        return new InvalidDimensionsException(dimension + " cannot be negative");
    }

    public static InvalidDimensionsException exceedsMax(String dimension, int maxMm) {
        return new InvalidDimensionsException(dimension + " cannot exceed " + maxMm + " mm");
    }
}
