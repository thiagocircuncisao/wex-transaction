package org.thiagocircuncisao.mapper;

import org.mapstruct.Mapper;
import org.thiagocircuncisao.model.Currency;
import org.thiagocircuncisao.presentation.CurrencyResponse;

@Mapper
public interface CurrencyMapper {
    CurrencyResponse toCurrencyResponse(Currency currency);
    Currency toCurrency(CurrencyResponse currencyResponse);
}
