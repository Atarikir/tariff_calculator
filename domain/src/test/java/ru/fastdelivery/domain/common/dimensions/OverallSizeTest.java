package ru.fastdelivery.domain.common.dimensions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class OverallSizeTest {

  @Test
  @DisplayName("Попытка создать отрицательный размер -> исключение")
  void whenMillimetersBelowZero_thenException() {
    BigInteger sizeMillimeters = new BigInteger("-1");
    assertThatThrownBy(() -> new OverallSize(sizeMillimeters))
        .isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void equalsTypeOverallSize_same() {
    OverallSize overallSize = new OverallSize(new BigInteger("1000"));
    OverallSize overallSizeSame = new OverallSize(new BigInteger("1000"));

    assertThat(overallSize).isEqualTo(overallSizeSame).hasSameHashCodeAs(overallSizeSame);
  }

  @Test
  void equalsNull_false() {
    OverallSize overallSize = new OverallSize(new BigInteger("100"));

    assertThat(overallSize).isNotEqualTo(null);
  }

  @ParameterizedTest
  @CsvSource({"1000, 1, -1", "199, 199, 0", "50, 999, 1"})
  void compareToTest(BigInteger low, BigInteger high, int expected) {
    OverallSize overallSizeLow = new OverallSize(low);
    OverallSize overallSizeHigh = new OverallSize(high);

    assertThat(overallSizeLow.compareTo(overallSizeHigh)).isEqualTo(expected);
  }

  @Test
  @DisplayName("Первый размер больше второго -> true")
  void whenFirstSizeGreaterThanSecond_thenTrue() {
    OverallSize sizeBig = new OverallSize(new BigInteger("1001"));
    OverallSize sizeSmall = new OverallSize(new BigInteger("1000"));

    assertThat(sizeBig.greaterThan(sizeSmall)).isTrue();
  }
}
