package ru.fastdelivery.presentation.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.fastdelivery.domain.exception.DomainException;
import ru.fastdelivery.domain.exception.InvalidCurrencyException;
import ru.fastdelivery.domain.exception.InvalidDimensionsException;
import ru.fastdelivery.domain.exception.InvalidGeoCoordinateException;
import ru.fastdelivery.domain.exception.InvalidWeightException;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidWeightException.class)
    public ResponseEntity<ApiError> handleInvalidWeight(InvalidWeightException e) {
        ApiError apiError = ApiError.badRequest(e.getMessage());
        return new ResponseEntity<>(apiError, apiError.httpStatus());
    }

    @ExceptionHandler(InvalidDimensionsException.class)
    public ResponseEntity<ApiError> handleInvalidDimensions(InvalidDimensionsException e) {
        ApiError apiError = ApiError.badRequest(e.getMessage());
        return new ResponseEntity<>(apiError, apiError.httpStatus());
    }

    @ExceptionHandler(InvalidGeoCoordinateException.class)
    public ResponseEntity<ApiError> handleInvalidGeoCoordinate(InvalidGeoCoordinateException e) {
        ApiError apiError = ApiError.badRequest(e.getMessage());
        return new ResponseEntity<>(apiError, apiError.httpStatus());
    }

    @ExceptionHandler(InvalidCurrencyException.class)
    public ResponseEntity<ApiError> handleInvalidCurrency(InvalidCurrencyException e) {
        ApiError apiError = ApiError.badRequest(e.getMessage());
        return new ResponseEntity<>(apiError, apiError.httpStatus());
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomainException(DomainException e) {
        ApiError apiError = ApiError.badRequest(e.getMessage());
        return new ResponseEntity<>(apiError, apiError.httpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ApiError apiError = ApiError.badRequest(errorMessage);
        return new ResponseEntity<>(apiError, apiError.httpStatus());
    }
}

