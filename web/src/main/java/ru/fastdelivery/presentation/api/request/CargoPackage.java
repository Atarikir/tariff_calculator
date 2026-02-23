package ru.fastdelivery.presentation.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.fastdelivery.presentation.validator.ValidPackageDimensions;

import java.math.BigInteger;

@Schema(description = "Упаковка груза")
@ValidPackageDimensions
public record CargoPackage(
        @Schema(description = "Вес упаковки, граммы", example = "5667")
        BigInteger weight,

        @Schema(description = "Длина упаковки, мм", example = "350")
        BigInteger length,

        @Schema(description = "Ширина упаковки, мм", example = "600")
        BigInteger width,

        @Schema(description = "Высота упаковки, мм", example = "250")
        BigInteger height
) {
}
