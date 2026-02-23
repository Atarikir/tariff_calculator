package ru.fastdelivery.domain.delivery.pack;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Объем упаковки в кубических метрах
 *
 * @param cubicMeters значение объема в м³
 */
public record Volume(BigDecimal cubicMeters) {

    private static final BigDecimal CUBIC_MM_TO_M3 = new BigDecimal("1000000000");
    private static final MathContext VOLUME_PRECISION = new MathContext(4, RoundingMode.HALF_UP);

    public Volume {
        if (cubicMeters.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Volume cannot be negative");
        }
    }

    /**
     * Рассчитывает объем из габаритов в миллиметрах
     * Формула: (length * width * height) / 1_000_000_000
     */
    public static Volume fromDimensions(OuterDimensions dimensions) {
        BigInteger volumeMm3 = dimensions.length().millimeters()
                .multiply(dimensions.width().millimeters())
                .multiply(dimensions.height().millimeters());

        BigDecimal cubicMeters = new BigDecimal(volumeMm3)
                .divide(CUBIC_MM_TO_M3, VOLUME_PRECISION);

        return new Volume(cubicMeters);
    }

    public static Volume zero() {
        return new Volume(BigDecimal.ZERO);
    }
}
