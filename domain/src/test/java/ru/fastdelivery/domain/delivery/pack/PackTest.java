package ru.fastdelivery.domain.delivery.pack;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.domain.common.weight.Weight;

import java.math.BigInteger;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PackTest {

    @Test
    @DisplayName("Вес больше 150 кг -> исключение")
    void whenWeightMoreThanMaxWeight_thenThrowException() {
        var weight = new Weight(BigInteger.valueOf(150_001));
        assertThatThrownBy(() -> new Pack(weight))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Вес меньше 150 кг -> упаковка создана")
    void whenWeightLessThanMaxWeight_thenObjectCreated() {
        var actual = new Pack(new Weight(BigInteger.valueOf(1_000)));
        assertThat(actual.weight()).isEqualTo(new Weight(BigInteger.valueOf(1_000)));
    }

    @Test
    @DisplayName("Создание упаковки без габаритов -> dimensions пустой")
    void whenCreateWithoutDimensions_thenEmpty() {
        var pack = new Pack(new Weight(BigInteger.valueOf(1_000)));
        assertThat(pack.dimensions()).isEmpty();
    }

    @Test
    @DisplayName("Создание упаковки с габаритами -> dimensions заполнен")
    void whenCreateWithDimensions_thenPresent() {
        var weight = new Weight(BigInteger.valueOf(1_000));
        var dimensions = OuterDimensions.fromMillimeters(
                BigInteger.valueOf(100),
                BigInteger.valueOf(100),
                BigInteger.valueOf(100));

        var pack = Pack.withDimensions(weight, dimensions);

        assertThat(pack.dimensions()).isPresent();
        assertThat(pack.dimensions().get()).isEqualTo(dimensions);
    }

    @Test
    @DisplayName("Конструктор с weight создает упаковку с пустым dimensions")
    void whenConstructorWithWeight_thenDimensionsEmpty() {
        var weight = new Weight(BigInteger.valueOf(5000));
        var pack = new Pack(weight);

        assertThat(pack.weight()).isEqualTo(weight);
        assertThat(pack.dimensions()).isEmpty();
    }
}