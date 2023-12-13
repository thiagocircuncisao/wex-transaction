package org.thiagocircuncisao.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Currency {
    private String country;
    private String currency;
    private Double exchangeRate;
    private String sourceLineNumber;
    private LocalDate recordDate;
}
