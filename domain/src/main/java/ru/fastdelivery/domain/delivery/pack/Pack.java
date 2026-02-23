package ru.fastdelivery.domain.delivery.pack;

import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.exception.InvalidWeightException;

import java.math.BigInteger;
import java.util.Optional;

/**
 * Упаковка груза
 *
 * @param weight      вес товаров в упаковке
 * @param dimensions  габариты упаковки (опционально)
 */
public record Pack(Weight weight, Optional<OuterDimensions> dimensions) {

    private static final Weight maxWeight = new Weight(BigInteger.valueOf(150_000));

    /**
     * Конструктор только с весом
     */
    public Pack(Weight weight) {
        this(weight, Optional.empty());
    }

    public Pack {
        if (weight.greaterThan(maxWeight)) {
            throw InvalidWeightException.exceedsMax(150_000);
        }
    }

    /**
     * Создает упаковку с габаритами
     */
    public static Pack withDimensions(Weight weight, OuterDimensions dimensions) {
        return new Pack(weight, Optional.of(dimensions));
    }
}
