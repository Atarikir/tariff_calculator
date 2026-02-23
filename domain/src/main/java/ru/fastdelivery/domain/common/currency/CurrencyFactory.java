package ru.fastdelivery.domain.common.currency;

import ru.fastdelivery.domain.exception.InvalidCurrencyException;

/**
 * Создание валюты с проверками
 */
public class CurrencyFactory {

    private final CurrencyPropertiesProvider propertiesProvider;

    public CurrencyFactory(CurrencyPropertiesProvider propertiesProvider) {
        this.propertiesProvider = propertiesProvider;
    }

    public Currency create(String code) {
        if (code == null || !propertiesProvider.isAvailable(code)) {
            throw InvalidCurrencyException.notAvailable(code);
        }

        return new Currency(code);
    }
}
