package ru.fastdelivery.domain.delivery.pack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VolumeTest {

    @Test
    @DisplayName("Попытка создать отрицательный объем -> исключение")
    void whenNegativeVolume_thenException() {
        assertThatThrownBy(() -> new Volume(new BigDecimal("-0.0001")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Создание нулевого объема -> успешно")
    void whenZeroVolume_thenCreated() {
        var volume = Volume.zero();
        assertThat(volume.cubicMeters()).isEqualTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Расчет объема из габаритов 350x600x250 мм -> 0.0525 м³")
    void whenCalculateVolumeFromExample_thenCorrect() {
        var dimensions = OuterDimensions.fromMillimeters(
                new java.math.BigInteger("350"),
                new java.math.BigInteger("600"),
                new java.math.BigInteger("250"));

        var volume = Volume.fromDimensions(dimensions);

        assertThat(volume.cubicMeters()).isEqualByComparingTo(new BigDecimal("0.0525"));
    }

    @Test
    @DisplayName("Расчет объема из габаритов 1000x1000x1000 мм -> 1 м³")
    void whenCalculateOneCubicMeter_thenCorrect() {
        var dimensions = OuterDimensions.fromMillimeters(
                new java.math.BigInteger("1000"),
                new java.math.BigInteger("1000"),
                new java.math.BigInteger("1000"));

        var volume = Volume.fromDimensions(dimensions);

        assertThat(volume.cubicMeters()).isEqualByComparingTo(new BigDecimal("1.0000"));
    }

    @Test
    @DisplayName("Расчет объема с точностью до 4 знаков")
    void whenCalculateVolume_thenFourDecimalPrecision() {
        var dimensions = OuterDimensions.fromMillimeters(
                new java.math.BigInteger("100"),
                new java.math.BigInteger("100"),
                new java.math.BigInteger("100"));

        var volume = Volume.fromDimensions(dimensions);

        assertThat(volume.cubicMeters()).isEqualByComparingTo(new BigDecimal("0.0010"));
    }
}
