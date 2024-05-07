package ru.fastdelivery.domain.common.dimensions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class DimensionsTest {

  private OverallSize length;
  private OverallSize width;
  private OverallSize height;

  @ParameterizedTest
  @CsvSource({"345, 589, 234, 0.0525", "450, 650, 550, 0.1609"})
  @DisplayName("Запрос объёма (куб м) -> получено корректное значение")
  void whenGetVolume_thenReceiveCubicMeters(
      BigInteger length, BigInteger width, BigInteger height, BigDecimal expected) {
    Dimensions dimensions =
        new Dimensions(
            new OverallSize(new BigInteger(String.valueOf(length))),
            new OverallSize(new BigInteger(String.valueOf(width))),
            new OverallSize(new BigInteger(String.valueOf(height))));

    BigDecimal actual = dimensions.cubicMeters();

    assertThat(actual).isEqualByComparingTo(new BigDecimal(String.valueOf(expected)));
  }

  @ParameterizedTest
  @CsvSource({"1501, 1500, 1500", "1500, 1501, 1500", "1500, 1500, 1501"})
  void whenLengthOrWidthOrHeightMoreThanMaxSize_thenThrowException(
      BigInteger l, BigInteger w, BigInteger h) {
    length = new OverallSize(l);
    width = new OverallSize(w);
    height = new OverallSize(h);

    assertThatThrownBy(() -> new Dimensions(length, width, height))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void whenLengthOrWidthOrHeightLessThanMaxSize_thenObjectCreated() {
    Dimensions actual =
        new Dimensions(
            new OverallSize(BigInteger.valueOf(1500)),
            new OverallSize(BigInteger.valueOf(1500)),
            new OverallSize(BigInteger.valueOf(1500)));
    assertThat(actual.length()).isEqualTo(new OverallSize(BigInteger.valueOf(1500)));
    assertThat(actual.width()).isEqualTo(new OverallSize(BigInteger.valueOf(1500)));
    assertThat(actual.height()).isEqualTo(new OverallSize(BigInteger.valueOf(1500)));
  }
}
