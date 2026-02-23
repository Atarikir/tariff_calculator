package ru.fastdelivery.presentation.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = GeoCoordinateValidator.class)
@Target({TYPE})
@Retention(RUNTIME)
public @interface ValidGeoCoordinate {
    String message() default "Invalid geographic coordinates";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
