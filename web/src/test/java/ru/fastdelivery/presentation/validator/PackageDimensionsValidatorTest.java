package ru.fastdelivery.presentation.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.fastdelivery.presentation.api.request.CargoPackage;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;

class PackageDimensionsValidatorTest {

    private final PackageDimensionsValidator validator = new PackageDimensionsValidator();

    @Test
    @DisplayName("null CargoPackage -> валиден")
    void whenNullPackage_thenValid() {
        assertThat(validator.isValid(null, null)).isTrue();
    }

    @Test
    @DisplayName("Упаковка без габаритов -> валидна")
    void whenPackageWithoutDimensions_thenValid() {
        var cargoPackage = new CargoPackage(BigInteger.valueOf(1000), null, null, null);
        assertThat(validator.isValid(cargoPackage, null)).isTrue();
    }

    @Test
    @DisplayName("Отрицательная длина -> не валидна")
    void whenNegativeLength_thenInvalid() {
        var cargoPackage = new CargoPackage(
                BigInteger.valueOf(1000),
                BigInteger.valueOf(-1),
                BigInteger.valueOf(100),
                BigInteger.valueOf(100));

        assertThat(validator.isValid(cargoPackage, null)).isFalse();
    }

    @Test
    @DisplayName("Длина > 1500 -> не валидна")
    void whenLengthExceeds1500_thenInvalid() {
        var cargoPackage = new CargoPackage(
                BigInteger.valueOf(1000),
                BigInteger.valueOf(1501),
                BigInteger.valueOf(100),
                BigInteger.valueOf(100));

        assertThat(validator.isValid(cargoPackage, null)).isFalse();
    }

    @Test
    @DisplayName("Ширина > 1500 -> не валидна")
    void whenWidthExceeds1500_thenInvalid() {
        var cargoPackage = new CargoPackage(
                BigInteger.valueOf(1000),
                BigInteger.valueOf(100),
                BigInteger.valueOf(1501),
                BigInteger.valueOf(100));

        assertThat(validator.isValid(cargoPackage, null)).isFalse();
    }

    @Test
    @DisplayName("Высота > 1500 -> не валидна")
    void whenHeightExceeds1500_thenInvalid() {
        var cargoPackage = new CargoPackage(
                BigInteger.valueOf(1000),
                BigInteger.valueOf(100),
                BigInteger.valueOf(100),
                BigInteger.valueOf(1501));

        assertThat(validator.isValid(cargoPackage, null)).isFalse();
    }

    @Test
    @DisplayName("Валидные габариты -> валидны")
    void whenValidDimensions_thenValid() {
        var cargoPackage = new CargoPackage(
                BigInteger.valueOf(1000),
                BigInteger.valueOf(350),
                BigInteger.valueOf(600),
                BigInteger.valueOf(250));

        assertThat(validator.isValid(cargoPackage, null)).isTrue();
    }

    @Test
    @DisplayName("Габариты на границе 1500 -> валидны")
    void whenDimensionsAtMaxBoundary_thenValid() {
        var cargoPackage = new CargoPackage(
                BigInteger.valueOf(1000),
                BigInteger.valueOf(1500),
                BigInteger.valueOf(1500),
                BigInteger.valueOf(1500));

        assertThat(validator.isValid(cargoPackage, null)).isTrue();
    }

    @Test
    @DisplayName("Габариты равны 0 -> валидны")
    void whenZeroDimensions_thenValid() {
        var cargoPackage = new CargoPackage(
                BigInteger.valueOf(1000),
                BigInteger.ZERO,
                BigInteger.ZERO,
                BigInteger.ZERO);

        assertThat(validator.isValid(cargoPackage, null)).isTrue();
    }
}
