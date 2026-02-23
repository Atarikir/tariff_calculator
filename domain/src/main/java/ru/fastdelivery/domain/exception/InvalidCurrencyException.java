package ru.fastdelivery.domain.exception;

/**
 * Исключение при некорректной валюте
 */
public class InvalidCurrencyException extends DomainException {
    public InvalidCurrencyException(String message) {
        super(message);
    }

    public static InvalidCurrencyException notAvailable(String code) {
        return new InvalidCurrencyException("Currency '" + code + "' is not available");
    }
}
