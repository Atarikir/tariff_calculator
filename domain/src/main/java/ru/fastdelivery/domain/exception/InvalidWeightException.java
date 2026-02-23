package ru.fastdelivery.domain.exception;

/**
 * Исключение при некорректном весе
 */
public class InvalidWeightException extends DomainException {
    public InvalidWeightException(String message) {
        super(message);
    }

    public static InvalidWeightException negative() {
        return new InvalidWeightException("Weight cannot be negative");
    }

    public static InvalidWeightException exceedsMax(int maxGrams) {
        return new InvalidWeightException("Package weight cannot exceed " + maxGrams + " grams");
    }
}
