package ru.fastdelivery.domain.delivery.pack;

import java.math.BigInteger;
import ru.fastdelivery.domain.common.dimensions.Dimensions;
import ru.fastdelivery.domain.common.weight.Weight;

/**
 * Упаковка груза
 *
 * @param weight вес товаров в упаковке
 */
public record Pack(Weight weight, Dimensions dimensions) {

  private static final Weight maxWeight = new Weight(BigInteger.valueOf(150_000));

  public Pack {
    if (weight.greaterThan(maxWeight)) {
      throw new IllegalArgumentException("Package can't be more than " + maxWeight);
    }
  }
}
