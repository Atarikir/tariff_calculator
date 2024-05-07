package ru.fastdelivery.domain.common.dimensions;

import java.math.BigInteger;

public record OverallSize(BigInteger millimeters) implements Comparable<OverallSize> {

  public OverallSize {
    if (isLessThanZero(millimeters)) {
      throw new IllegalArgumentException("Weight cannot be below Zero!");
    }
  }

  private static boolean isLessThanZero(BigInteger price) {
    return BigInteger.ZERO.compareTo(price) > 0;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    OverallSize size = (OverallSize) o;
    return millimeters.compareTo(size.millimeters) == 0;
  }

  @Override
  public int compareTo(OverallSize size) {
    return size.millimeters().compareTo(millimeters());
  }

  public boolean greaterThan(OverallSize size) {
    return millimeters().compareTo(size.millimeters()) > 0;
  }
}
