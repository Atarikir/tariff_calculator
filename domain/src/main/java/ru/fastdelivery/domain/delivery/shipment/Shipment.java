package ru.fastdelivery.domain.delivery.shipment;

import ru.fastdelivery.domain.common.currency.Currency;
import ru.fastdelivery.domain.common.weight.Weight;
import ru.fastdelivery.domain.delivery.pack.Pack;
import ru.fastdelivery.domain.delivery.route.Route;

import java.util.List;
import java.util.Optional;

/**
 * Отправление с упаковками и маршрутом
 *
 * @param packages упаковки в грузе
 * @param currency валюта объявленная для груза
 * @param route    маршрут доставки (опционально)
 */
public record Shipment(
        List<Pack> packages,
        Currency currency,
        Optional<Route> route
) {
    /**
     * Конструктор без маршрута
     */
    public Shipment(List<Pack> packages, Currency currency) {
        this(packages, currency, Optional.empty());
    }

    public Weight weightAllPackages() {
        return packages.stream()
                .map(Pack::weight)
                .reduce(Weight.zero(), Weight::add);
    }
}
