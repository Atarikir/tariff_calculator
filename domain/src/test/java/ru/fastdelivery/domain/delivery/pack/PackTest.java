package ru.fastdelivery.domain.delivery.pack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static ru.fastdelivery.domain.delivery.Data.dimensions;

import java.math.BigInteger;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.weight.Weight;

class PackTest {

  @Test
  void whenWeightMoreThanMaxWeight_thenThrowException() {
    var weight = new Weight(BigInteger.valueOf(150_001));
    assertThatThrownBy(() -> new Pack(weight, dimensions()))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void whenWeightLessThanMaxWeight_thenObjectCreated() {
    var actual = new Pack(new Weight(BigInteger.valueOf(1_000)), dimensions());
    assertThat(actual.weight()).isEqualTo(new Weight(BigInteger.valueOf(1_000)));
  }
}
