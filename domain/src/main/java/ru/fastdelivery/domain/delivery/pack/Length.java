package ru.fastdelivery.domain.delivery.pack;

import ru.fastdelivery.domain.exception.InvalidDimensionsException;

import java.math.BigInteger;

/**
 * Класс для представления длины (ширины, высоты) в миллиметрах
 *
 * @param millimeters значение в миллиметрах
 */
public record Length(BigInteger millimeters) implements Comparable<Length> {

    private static final BigInteger ZERO = BigInteger.ZERO;

    public Length {
        if (millimeters.compareTo(ZERO) < 0) {
            throw InvalidDimensionsException.negative("Length");
        }
    }

    public static Length zero() {
        return new Length(ZERO);
    }

    public boolean isLongerThan(Length other) {
        return this.millimeters.compareTo(other.millimeters) > 0;
    }

    @Override
    public int compareTo(Length other) {
        return this.millimeters.compareTo(other.millimeters);
    }
}
