package ru.fastdelivery.presentation.calc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.geo.GeoCoordinate;
import ru.fastdelivery.domain.delivery.pack.OuterDimensions;
import ru.fastdelivery.domain.delivery.pack.OuterDimensionsFactory;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.route.Route;
import ru.fastdelivery.domain.delivery.shipment.Shipment;
import ru.fastdelivery.presentation.api.request.CalculatePackagesRequest;
import ru.fastdelivery.presentation.api.request.CargoPackage;
import ru.fastdelivery.presentation.api.request.GeoPoint;
import ru.fastdelivery.presentation.api.response.CalculatePackagesResponse;
import ru.fastdelivery.usecase.TariffCalculateUseCase;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/calculate/")
@RequiredArgsConstructor
@Tag(name = "Расчеты стоимости доставки")
public class CalculateController {
    private final TariffCalculateUseCase tariffCalculateUseCase;
    private final CurrencyFactory currencyFactory;

    @PostMapping
    @Operation(summary = "Расчет стоимости по упаковкам груза")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided")
    })
    public CalculatePackagesResponse calculate(
            @Valid @RequestBody CalculatePackagesRequest request) {
        var packsWeights = request.packages().stream()
                .map(this::createPack)
                .toList();

        var currency = currencyFactory.create(request.currencyCode());
        var route = createRoute(request.departure(), request.destination());

        var shipment = route
                .map(r -> new Shipment(packsWeights, currency, Optional.of(r)))
                .orElse(new Shipment(packsWeights, currency));

        var calculatedPrice = tariffCalculateUseCase.calc(shipment);
        var minimalPrice = tariffCalculateUseCase.minimalPrice();
        return new CalculatePackagesResponse(calculatedPrice, minimalPrice);
    }

    private Pack createPack(CargoPackage cargoPackage) {
        var weight = new Weight(cargoPackage.weight());

        if (hasDimensions(cargoPackage)) {
            var dimensions = OuterDimensionsFactory.createNormalized(
                    cargoPackage.length(),
                    cargoPackage.width(),
                    cargoPackage.height());
            return Pack.withDimensions(weight, dimensions);
        }

        return new Pack(weight);
    }

    private boolean hasDimensions(CargoPackage cargoPackage) {
        return cargoPackage.length() != null
                && cargoPackage.width() != null
                && cargoPackage.height() != null;
    }

    private Optional<Route> createRoute(GeoPoint departurePoint, GeoPoint destinationPoint) {
        if (departurePoint == null || destinationPoint == null) {
            return Optional.empty();
        }

        var departure = new GeoCoordinate(departurePoint.latitude(), departurePoint.longitude());
        var destination = new GeoCoordinate(destinationPoint.latitude(), destinationPoint.longitude());

        return Optional.of(new Route(departure, destination));
    }
}
