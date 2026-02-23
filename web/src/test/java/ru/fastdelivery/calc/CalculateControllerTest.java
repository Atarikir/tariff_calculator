package ru.fastdelivery.calc;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.fastdelivery.ControllerTest;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.presentation.api.request.CalculatePackagesRequest;
import ru.fastdelivery.presentation.api.request.CargoPackage;
import ru.fastdelivery.presentation.api.request.GeoPoint;
import ru.fastdelivery.presentation.api.response.CalculatePackagesResponse;
import ru.fastdelivery.usecase.TariffCalculateUseCase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CalculateControllerTest extends ControllerTest {

    final String baseCalculateApi = "/api/v1/calculate/";
    @MockBean
    TariffCalculateUseCase useCase;
    @MockBean
    CurrencyFactory currencyFactory;

    @Test
    @DisplayName("Валидные данные для расчета стоимость -> Ответ 200")
    void whenValidInputData_thenReturn200() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, null, null, null)),
                "RUB",
                null,
                null);
        var rub = new CurrencyFactory(code -> true).create("RUB");
        when(useCase.calc(any())).thenReturn(new Price(BigDecimal.valueOf(10), rub));
        when(useCase.minimalPrice()).thenReturn(new Price(BigDecimal.valueOf(5), rub));

        ResponseEntity<CalculatePackagesResponse> response =
                restTemplate.postForEntity(baseCalculateApi, request, CalculatePackagesResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Список упаковок == null -> Ответ 400")
    void whenEmptyListPackages_thenReturn400() {
        var request = new CalculatePackagesRequest(null, "RUB", null, null);

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Валидные данные с габаритами -> Ответ 200")
    void whenValidInputWithDimensions_thenReturn200() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(
                        BigInteger.valueOf(1000),
                        BigInteger.valueOf(350),
                        BigInteger.valueOf(600),
                        BigInteger.valueOf(250))),
                "RUB",
                null,
                null);
        var rub = new CurrencyFactory(code -> true).create("RUB");
        when(useCase.calc(any())).thenReturn(new Price(BigDecimal.valueOf(10), rub));
        when(useCase.minimalPrice()).thenReturn(new Price(BigDecimal.valueOf(5), rub));

        ResponseEntity<CalculatePackagesResponse> response =
                restTemplate.postForEntity(baseCalculateApi, request, CalculatePackagesResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Валидные данные с координатами -> Ответ 200")
    void whenValidInputWithCoordinates_thenReturn200() {
        var departure = new GeoPoint(new BigDecimal("55.0"), new BigDecimal("50.0"));
        var destination = new GeoPoint(new BigDecimal("60.0"), new BigDecimal("55.0"));

        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, null, null, null)),
                "RUB",
                destination,
                departure);
        var rub = new CurrencyFactory(code -> true).create("RUB");
        when(useCase.calc(any())).thenReturn(new Price(BigDecimal.valueOf(10), rub));
        when(useCase.minimalPrice()).thenReturn(new Price(BigDecimal.valueOf(5), rub));

        ResponseEntity<CalculatePackagesResponse> response =
                restTemplate.postForEntity(baseCalculateApi, request, CalculatePackagesResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Широта < 45 -> Ответ 400")
    void whenLatitudeBelow45_thenReturn400() {
        var departure = new GeoPoint(new BigDecimal("44.999"), new BigDecimal("50.0"));
        var destination = new GeoPoint(new BigDecimal("60.0"), new BigDecimal("55.0"));

        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(BigInteger.TEN, null, null, null)),
                "RUB",
                destination,
                departure);

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @DisplayName("Длина > 1500 -> Ответ 400")
    void whenLengthExceeds1500_thenReturn400() {
        var request = new CalculatePackagesRequest(
                List.of(new CargoPackage(
                        BigInteger.valueOf(1000),
                        BigInteger.valueOf(1501),
                        BigInteger.valueOf(100),
                        BigInteger.valueOf(100))),
                "RUB",
                null,
                null);

        ResponseEntity<String> response = restTemplate.postForEntity(baseCalculateApi, request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
