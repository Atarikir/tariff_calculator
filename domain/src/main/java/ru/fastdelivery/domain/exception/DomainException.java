package ru.fastdelivery.domain.exception;

/**
 * Базовое исключение для предметной области
 */
public class DomainException extends IllegalArgumentException {
    public DomainException(String message) {
        super(message);
    }
}
