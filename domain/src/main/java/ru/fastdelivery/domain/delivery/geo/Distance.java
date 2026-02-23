package ru.fastdelivery.domain.delivery.geo;

import java.math.BigDecimal;

/**
 * Расстояние между двумя географическими координатами в километрах
 *
 * @param kilometers расстояние в километрах
 */
public record Distance(BigDecimal kilometers) {

    public Distance {
        if (kilometers.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Distance cannot be negative");
        }
    }

    /**
     * Вычисляет расстояние между двумя координатами по формуле гаверсинусов
     * с модификацией для антиподов
     */
    public static Distance between(GeoCoordinate from, GeoCoordinate to) {
        double lat1Rad = Math.toRadians(from.latitude().doubleValue());
        double lat2Rad = Math.toRadians(to.latitude().doubleValue());
        double deltaLat = Math.toRadians(to.latitude().subtract(from.latitude()).doubleValue());
        double deltaLon = Math.toRadians(to.longitude().subtract(from.longitude()).doubleValue());

        double earthRadiusKm = 6371.0;

        // Формула гаверсинусов
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        // Модификация для антиподов - используем haversine формулу напрямую
        double distance = earthRadiusKm * c;

        return new Distance(new BigDecimal(String.valueOf(distance)));
    }

    public static Distance zero() {
        return new Distance(BigDecimal.ZERO);
    }
}
