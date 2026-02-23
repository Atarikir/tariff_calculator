package ru.fastdelivery.domain.delivery.geo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.within;

class DistanceTest {

    @Test
    @DisplayName("Попытка создать отрицательное расстояние -> исключение")
    void whenNegativeDistance_thenException() {
        assertThatThrownBy(() -> new Distance(new BigDecimal("-1")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Создание нулевого расстояния -> успешно")
    void whenZeroDistance_thenCreated() {
        var distance = Distance.zero();
        assertThat(distance.kilometers()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    @DisplayName("Расстояние между одинаковыми координатами -> 0")
    void whenSameCoordinates_thenZeroDistance() {
        var coord = new GeoCoordinate(new BigDecimal("55.7558"), new BigDecimal("37.6173"));
        var distance = Distance.between(coord, coord);

        assertThat(distance.kilometers()).isCloseTo(BigDecimal.ZERO, within(new BigDecimal("0.01")));
    }

    @Test
    @DisplayName("Расчет расстояния между Москвой и Санкт-Петербургом")
    void whenCalculateMoscowToSpb_thenCorrect() {
        var moscow = new GeoCoordinate(new BigDecimal("55.7558"), new BigDecimal("37.6173"));
        var spb = new GeoCoordinate(new BigDecimal("59.9343"), new BigDecimal("30.3351"));

        var distance = Distance.between(moscow, spb);

        assertThat(distance.kilometers()).isCloseTo(new BigDecimal("635"), within(new BigDecimal("10")));
    }

    @Test
    @DisplayName("Расчет расстояния по примеру из ТЗ (в пределах допустимых координат)")
    void whenCalculateExampleFromTask() {
        var departure = new GeoCoordinate(new BigDecimal("55.446008"), new BigDecimal("65.339151"));
        var destination = new GeoCoordinate(new BigDecimal("65.0"), new BigDecimal("55.027532"));

        var distance = Distance.between(departure, destination);

        assertThat(distance.kilometers()).isGreaterThan(new BigDecimal("100"));
    }

    @Test
    @DisplayName("Расстояние между точками с разницей 1 градус широты в допустимом диапазоне")
    void whenOneDegreeLatitudeDifference() {
        var point1 = new GeoCoordinate(new BigDecimal("50"), new BigDecimal("50"));
        var point2 = new GeoCoordinate(new BigDecimal("51"), new BigDecimal("50"));

        var distance = Distance.between(point1, point2);

        assertThat(distance.kilometers()).isCloseTo(new BigDecimal("111"), within(new BigDecimal("1")));
    }
}
