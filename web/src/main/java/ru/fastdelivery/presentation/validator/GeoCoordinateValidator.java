package ru.fastdelivery.presentation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.fastdelivery.presentation.api.request.GeoPoint;

import java.math.BigDecimal;

public class GeoCoordinateValidator implements ConstraintValidator<ValidGeoCoordinate, GeoPoint> {

    private static final BigDecimal MIN_LATITUDE = new BigDecimal("45");
    private static final BigDecimal MAX_LATITUDE = new BigDecimal("65");
    private static final BigDecimal MIN_LONGITUDE = new BigDecimal("30");
    private static final BigDecimal MAX_LONGITUDE = new BigDecimal("96");

    @Override
    public boolean isValid(GeoPoint geoPoint, ConstraintValidatorContext context) {
        if (geoPoint == null) {
            return true;
        }

        if (geoPoint.latitude() == null || geoPoint.longitude() == null) {
            return false;
        }

        boolean validLatitude = validateLatitude(geoPoint.latitude(), context);
        boolean validLongitude = validateLongitude(geoPoint.longitude(), context);

        return validLatitude && validLongitude;
    }

    private boolean validateLatitude(BigDecimal latitude, ConstraintValidatorContext context) {
        if (latitude.compareTo(MIN_LATITUDE) < 0 || latitude.compareTo(MAX_LATITUDE) > 0) {
            if (context != null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        String.format("Latitude (%.4f) is out of range [%.0f, %.0f]",
                                latitude.doubleValue(), MIN_LATITUDE.doubleValue(), MAX_LATITUDE.doubleValue()))
                        .addConstraintViolation();
            }
            return false;
        }
        return true;
    }

    private boolean validateLongitude(BigDecimal longitude, ConstraintValidatorContext context) {
        if (longitude.compareTo(MIN_LONGITUDE) < 0 || longitude.compareTo(MAX_LONGITUDE) > 0) {
            if (context != null) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                        String.format("Longitude (%.4f) is out of range [%.0f, %.0f]",
                                longitude.doubleValue(), MIN_LONGITUDE.doubleValue(), MAX_LONGITUDE.doubleValue()))
                        .addConstraintViolation();
            }
            return false;
        }
        return true;
    }
}
