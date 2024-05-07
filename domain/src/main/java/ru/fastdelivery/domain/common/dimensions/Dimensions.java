package ru.fastdelivery.domain.common.dimensions;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public record Dimensions(OverallSize length, OverallSize width, OverallSize height) {

  private static final OverallSize maxSize = new OverallSize(BigInteger.valueOf(1500));

  public Dimensions {
    if (length.greaterThan(maxSize)) {
      throw new IllegalArgumentException("Length can't be more than " + maxSize);
    }
    if (width.greaterThan(maxSize)) {
      throw new IllegalArgumentException("Width can't be more than " + maxSize);
    }
    if (height.greaterThan(maxSize)) {
      throw new IllegalArgumentException("Height can't be more than " + maxSize);
    }
  }

  public BigDecimal cubicMeters() {
    BigInteger length = multiplesOfFifty(length().millimeters());
    BigInteger width = multiplesOfFifty(width().millimeters());
    BigInteger height = multiplesOfFifty(height().millimeters());
    BigInteger volume = length.multiply(width).multiply(height);
    return new BigDecimal(volume)
        .divide(BigDecimal.valueOf(1_000_000_000), 4, RoundingMode.HALF_UP);
  }

  /** Перед расчётом объёма округляем каждое значение габарита упаковки кратно 50 */
  private BigInteger multiplesOfFifty(BigInteger bigInteger) {
    double number = bigInteger.intValue();
    return BigInteger.valueOf(
        ((number / 50) % 1 != 0 ? ((int) (number / 50 + 1) * 50) : (int) number));
  }
}
