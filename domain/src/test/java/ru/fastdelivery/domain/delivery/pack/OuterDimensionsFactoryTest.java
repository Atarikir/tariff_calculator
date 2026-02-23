package ru.fastdelivery.domain.delivery.pack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class OuterDimensionsFactoryTest {

    @ParameterizedTest
    @CsvSource({
            "0, 0, 0, 0, 0, 0",
            "1, 1, 1, 50, 50, 50",
            "49, 49, 49, 50, 50, 50",
            "50, 50, 50, 50, 50, 50",
            "51, 51, 51, 100, 100, 100",
            "100, 100, 100, 100, 100, 100",
            "345, 589, 234, 350, 600, 250",
            "1500, 1500, 1500, 1500, 1500, 1500"
    })
    @DisplayName("Округление габаритов до 50 мм в большую сторону")
    void whenRoundUpTo50_thenCorrect(
            BigInteger length, BigInteger width, BigInteger height,
            BigInteger expectedLength, BigInteger expectedWidth, BigInteger expectedHeight) {

        var dimensions = OuterDimensionsFactory.createNormalized(length, width, height);

        assertThat(dimensions.length().millimeters()).isEqualTo(expectedLength);
        assertThat(dimensions.width().millimeters()).isEqualTo(expectedWidth);
        assertThat(dimensions.height().millimeters()).isEqualTo(expectedHeight);
    }

    @Test
    @DisplayName("Создание нормализованных габаритов из примера ТЗ")
    void whenCreateFromTaskExample_thenCorrect() {
        var dimensions = OuterDimensionsFactory.createNormalized(
                BigInteger.valueOf(345),
                BigInteger.valueOf(589),
                BigInteger.valueOf(234));

        assertThat(dimensions.length().millimeters()).isEqualTo(BigInteger.valueOf(350));
        assertThat(dimensions.width().millimeters()).isEqualTo(BigInteger.valueOf(600));
        assertThat(dimensions.height().millimeters()).isEqualTo(BigInteger.valueOf(250));
    }
}
