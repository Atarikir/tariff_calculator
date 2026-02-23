package ru.fastdelivery.domain.delivery.pack;

import ru.fastdelivery.domain.exception.InvalidDimensionsException;

import java.math.BigInteger;

/**
 * Габариты упаковки
 *
 * @param length Длина в миллиметрах
 * @param width  Ширина в миллиметрах
 * @param height Высота в миллиметрах
 */
public record OuterDimensions(
        Length length,
        Length width,
        Length height
) {

    private static final Length MAX_DIMENSION = new Length(BigInteger.valueOf(1500));

    public OuterDimensions {
        validateDimension(length, "Length");
        validateDimension(width, "Width");
        validateDimension(height, "Height");
    }

    private static void validateDimension(Length dimension, String name) {
        if (dimension.isLongerThan(MAX_DIMENSION)) {
            throw InvalidDimensionsException.exceedsMax(name.toLowerCase(), 1500);
        }
    }

    /**
     * Создает габариты из значений в миллиметрах
     */
    public static OuterDimensions fromMillimeters(BigInteger length, BigInteger width, BigInteger height) {
        return new OuterDimensions(new Length(length), new Length(width), new Length(height));
    }
}
