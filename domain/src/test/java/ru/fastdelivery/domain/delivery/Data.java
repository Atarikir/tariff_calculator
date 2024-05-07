package ru.fastdelivery.domain.delivery;

import java.math.BigInteger;
import ru.fastdelivery.domain.common.dimensions.Dimensions;
import ru.fastdelivery.domain.common.dimensions.OverallSize;

public class Data {

  public static Dimensions dimensions() {
    return new Dimensions(
        new OverallSize(BigInteger.valueOf(100)),
        new OverallSize(BigInteger.valueOf(100)),
        new OverallSize(BigInteger.valueOf(100)));
  }
}
