package ru.fastdelivery.domain.delivery.route;

import ru.fastdelivery.domain.delivery.geo.GeoCoordinate;

/**
 * Маршрут доставки с точкой отправления и назначения
 *
 * @param departure   координаты пункта отправления
 * @param destination координаты пункта назначения
 */
public record Route(
        GeoCoordinate departure,
        GeoCoordinate destination
) {
    public Route {
        if (departure == null || destination == null) {
            throw new IllegalArgumentException("Departure and destination must not be null");
        }
    }
}
