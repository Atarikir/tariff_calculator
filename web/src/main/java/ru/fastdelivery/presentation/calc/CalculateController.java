package ru.fastdelivery.presentation.calc;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fastdelivery.domain.common.currency.CurrencyFactory;
import ru.fastdelivery.domain.common.dimensions.Dimensions;
import ru.fastdelivery.domain.common.dimensions.OverallSize;
import ru.fastdelivery.domain.common.price.Price;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.shipment.Shipment;
import ru.fastdelivery.presentation.api.request.CalculatePackagesRequest;
import ru.fastdelivery.presentation.api.request.CargoPackage;
import ru.fastdelivery.presentation.api.response.CalculatePackagesResponse;
import ru.fastdelivery.usecase.TariffCalculateUseCase;

@RestController
@RequestMapping("/api/v1/calculate/")
@RequiredArgsConstructor
@Tag(name = "Расчеты стоимости доставки")
public class CalculateController {
  private final TariffCalculateUseCase tariffCalculateUseCase;
  private final CurrencyFactory currencyFactory;

  @PostMapping
  @Operation(summary = "Расчет стоимости по упаковкам груза")
  @ApiResponses(
      value = {
        @ApiResponse(responseCode = "200", description = "Successful operation"),
        @ApiResponse(responseCode = "400", description = "Invalid input provided")
      })
  public CalculatePackagesResponse calculate(@Valid @RequestBody CalculatePackagesRequest request) {
    List<Pack> packsWeights = new ArrayList<>();
    for (CargoPackage cargoPackage : request.packages()) {
      BigInteger weight = cargoPackage.weight();
      Weight weight1 = new Weight(weight);
      OverallSize length = new OverallSize(cargoPackage.length());
      OverallSize width = new OverallSize(cargoPackage.width());
      OverallSize height = new OverallSize(cargoPackage.height());
      Dimensions dimensions = new Dimensions(length, width, height);
      Pack pack = new Pack(weight1, dimensions);
      packsWeights.add(pack);
    }

    Shipment shipment = new Shipment(packsWeights, currencyFactory.create(request.currencyCode()));
    Price calculatedPrice = tariffCalculateUseCase.calc(shipment);
    Price minimalPrice = tariffCalculateUseCase.minimalPrice();
    return new CalculatePackagesResponse(calculatedPrice, minimalPrice);
  }
}
