package ru.fastdelivery.presentation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import ru.fastdelivery.presentation.validator.ValidGeoCoordinate;

import java.math.BigDecimal;

@Schema(description = "Географическая координата")
@ValidGeoCoordinate
public record GeoPoint(
        @Schema(description = "Широта", example = "55.7558")
        @NotNull
        BigDecimal latitude,

        @Schema(description = "Долгота", example = "37.6173")
        @NotNull
        BigDecimal longitude
) {
}
