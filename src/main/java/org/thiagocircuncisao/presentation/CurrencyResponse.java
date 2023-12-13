package org.thiagocircuncisao.presentation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyResponse {
    private String country;
    private String currency;
    private Double exchangeRate;
    private String sourceLineNumber;
    private LocalDate recordDate;
}
