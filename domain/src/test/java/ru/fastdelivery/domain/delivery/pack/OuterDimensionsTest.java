package ru.fastdelivery.domain.delivery.pack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OuterDimensionsTest {

    @Test
    @DisplayName("Создание габаритов с отрицательной длиной -> исключение")
    void whenNegativeLength_thenException() {
        assertThatThrownBy(() -> OuterDimensions.fromMillimeters(
                        BigInteger.valueOf(-1),
                        BigInteger.valueOf(100),
                        BigInteger.valueOf(100)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Создание габаритов с длиной > 1500 -> исключение")
    void whenLengthExceeds1500_thenException() {
        assertThatThrownBy(() -> OuterDimensions.fromMillimeters(
                        BigInteger.valueOf(1501),
                        BigInteger.valueOf(100),
                        BigInteger.valueOf(100)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Создание габаритов с шириной > 1500 -> исключение")
    void whenWidthExceeds1500_thenException() {
        assertThatThrownBy(() -> OuterDimensions.fromMillimeters(
                        BigInteger.valueOf(100),
                        BigInteger.valueOf(1501),
                        BigInteger.valueOf(100)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Создание габаритов с высотой > 1500 -> исключение")
    void whenHeightExceeds1500_thenException() {
        assertThatThrownBy(() -> OuterDimensions.fromMillimeters(
                        BigInteger.valueOf(100),
                        BigInteger.valueOf(100),
                        BigInteger.valueOf(1501)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Создание габаритов с максимальным значением 1500 -> успешно")
    void whenMaxDimension1500_thenCreated() {
        var dimensions = OuterDimensions.fromMillimeters(
                BigInteger.valueOf(1500),
                BigInteger.valueOf(1500),
                BigInteger.valueOf(1500));

        assertThat(dimensions.length().millimeters()).isEqualTo(BigInteger.valueOf(1500));
        assertThat(dimensions.width().millimeters()).isEqualTo(BigInteger.valueOf(1500));
        assertThat(dimensions.height().millimeters()).isEqualTo(BigInteger.valueOf(1500));
    }

    @Test
    @DisplayName("Создание габаритов с нулевыми значениями -> успешно")
    void whenZeroDimensions_thenCreated() {
        var dimensions = OuterDimensions.fromMillimeters(
                BigInteger.ZERO,
                BigInteger.ZERO,
                BigInteger.ZERO);

        assertThat(dimensions.length().millimeters()).isEqualTo(BigInteger.ZERO);
        assertThat(dimensions.width().millimeters()).isEqualTo(BigInteger.ZERO);
        assertThat(dimensions.height().millimeters()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    @DisplayName("Создание габаритов с нормальными значениями -> успешно")
    void whenNormalDimensions_thenCreated() {
        var dimensions = OuterDimensions.fromMillimeters(
                BigInteger.valueOf(350),
                BigInteger.valueOf(600),
                BigInteger.valueOf(250));

        assertThat(dimensions.length().millimeters()).isEqualTo(BigInteger.valueOf(350));
        assertThat(dimensions.width().millimeters()).isEqualTo(BigInteger.valueOf(600));
        assertThat(dimensions.height().millimeters()).isEqualTo(BigInteger.valueOf(250));
    }
}
