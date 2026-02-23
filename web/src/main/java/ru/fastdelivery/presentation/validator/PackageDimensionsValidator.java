package ru.fastdelivery.presentation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.fastdelivery.presentation.api.request.CargoPackage;

import java.math.BigInteger;

public class PackageDimensionsValidator implements ConstraintValidator<ValidPackageDimensions, CargoPackage> {

    private static final BigInteger ZERO = BigInteger.ZERO;
    private static final BigInteger MAX_DIMENSION = BigInteger.valueOf(1500);

    @Override
    public boolean isValid(CargoPackage cargoPackage, ConstraintValidatorContext context) {
        if (cargoPackage == null) {
            return true;
        }

        boolean hasAllDimensions = cargoPackage.length() != null
                && cargoPackage.width() != null
                && cargoPackage.height() != null;

        if (!hasAllDimensions) {
            return true;
        }

        boolean validLength = validateDimension(cargoPackage.length(), "length", context);
        boolean validWidth = validateDimension(cargoPackage.width(), "width", context);
        boolean validHeight = validateDimension(cargoPackage.height(), "height", context);

        return validLength && validWidth && validHeight;
    }

    private boolean validateDimension(BigInteger dimension, String fieldName, ConstraintValidatorContext context) {
        if (dimension.compareTo(ZERO) < 0) {
            if (context != null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        String.format("%s (%d) cannot be negative, must be >= 0", fieldName, dimension))
                        .addConstraintViolation();
            }
            return false;
        }

        if (dimension.compareTo(MAX_DIMENSION) > 0) {
            if (context != null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        String.format("%s (%d) exceeds maximum allowed value of %d mm",
                                fieldName, dimension, MAX_DIMENSION))
                        .addConstraintViolation();
            }
            return false;
        }

        return true;
    }
}
