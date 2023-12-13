package org.thiagocircuncisao.repository.impl;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.Map;

@Data
@Repository
public class CurrencyRepositoryImpl {
    @Value("${currency-api.url}")
    private String currencyApiUrl;

    public Map getCurrencies() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json;charset=UTF-8");
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = currencyApiUrl + "?filter=record_date:gte:" + LocalDate.now().minusMonths(6);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, Map.class).getBody();
    }

    public Map getCurrency(String country, String currencyName) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json;charset=UTF-8");
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = currencyApiUrl + "?filter=country:eq:" + country
                + ",currency:eq:" + currencyName
                +",record_date:gte:" + LocalDate.now().minusMonths(6);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        return restTemplate.exchange(url, HttpMethod.GET, entity, Map.class).getBody();
    }
}
