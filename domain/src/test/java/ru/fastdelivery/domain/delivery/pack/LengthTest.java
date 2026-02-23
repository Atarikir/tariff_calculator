package ru.fastdelivery.domain.delivery.pack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LengthTest {

    @Test
    @DisplayName("Попытка создать отрицательную длину -> исключение")
    void whenNegativeLength_thenException() {
        assertThatThrownBy(() -> new Length(BigInteger.valueOf(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Создание длины с нулевым значением -> успешно")
    void whenZeroLength_thenCreated() {
        var length = new Length(BigInteger.ZERO);
        assertThat(length.millimeters()).isEqualTo(BigInteger.ZERO);
    }

    @Test
    @DisplayName("Создание положительной длины -> успешно")
    void whenPositiveLength_thenCreated() {
        var length = new Length(BigInteger.valueOf(100));
        assertThat(length.millimeters()).isEqualTo(BigInteger.valueOf(100));
    }

    @Test
    @DisplayName("Длина больше другой -> isLongerThan возвращает true")
    void whenLengthIsLonger_thenReturnsTrue() {
        var length1 = new Length(BigInteger.valueOf(200));
        var length2 = new Length(BigInteger.valueOf(100));

        assertThat(length1.isLongerThan(length2)).isTrue();
    }

    @Test
    @DisplayName("Длина меньше другой -> isLongerThan возвращает false")
    void whenLengthIsShorter_thenReturnsFalse() {
        var length1 = new Length(BigInteger.valueOf(100));
        var length2 = new Length(BigInteger.valueOf(200));

        assertThat(length1.isLongerThan(length2)).isFalse();
    }

    @ParameterizedTest
    @CsvSource({ "100, 200, -1",
            "100, 100, 0",
            "200, 100, 1" })
    @DisplayName("Сравнение длин через compareTo")
    void compareToTest(BigInteger val1, BigInteger val2, int expected) {
        var length1 = new Length(val1);
        var length2 = new Length(val2);

        assertThat(length1.compareTo(length2)).isEqualTo(expected);
    }

    @Test
    @DisplayName("Length.zero() возвращает ноль")
    void whenZeroStaticMethod_thenReturnsZero() {
        assertThat(Length.zero().millimeters()).isEqualTo(BigInteger.ZERO);
    }
}
