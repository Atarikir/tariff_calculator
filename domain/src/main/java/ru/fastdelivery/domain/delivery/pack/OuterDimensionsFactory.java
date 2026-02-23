package ru.fastdelivery.domain.delivery.pack;

import java.math.BigInteger;

/**
 * Фабрика для создания нормализованных габаритов
 * Округляет значения до ближайших 50 мм в большую сторону
 */
public final class OuterDimensionsFactory {

    private static final BigInteger ROUNDING_STEP = BigInteger.valueOf(50);

    private OuterDimensionsFactory() {
        // Utility class
    }

    /**
     * Создает нормализованные габариты с округлением до 50 мм
     */
    public static OuterDimensions createNormalized(
            BigInteger length,
            BigInteger width,
            BigInteger height) {

        BigInteger normalizedLength = roundUpTo50(length);
        BigInteger normalizedWidth = roundUpTo50(width);
        BigInteger normalizedHeight = roundUpTo50(height);

        return OuterDimensions.fromMillimeters(normalizedLength, normalizedWidth, normalizedHeight);
    }

    /**
     * Округляет значение до ближайших 50 в большую сторону
     */
    private static BigInteger roundUpTo50(BigInteger value) {
        if (value.compareTo(BigInteger.ZERO) <= 0) {
            return BigInteger.ZERO;
        }
        BigInteger[] divRem = value.divideAndRemainder(ROUNDING_STEP);
        if (divRem[1].compareTo(BigInteger.ZERO) == 0) {
            return value;
        }
        return divRem[0].add(BigInteger.ONE).multiply(ROUNDING_STEP);
    }
}
