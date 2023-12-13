package org.thiagocircuncisao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thiagocircuncisao.exception.TransactionException;
import org.thiagocircuncisao.mapper.CurrencyMapper;
import org.thiagocircuncisao.mapper.CurrencyMapperImpl;
import org.thiagocircuncisao.mapper.TransactionMapper;
import org.thiagocircuncisao.mapper.TransactionMapperImpl;
import org.thiagocircuncisao.model.Currency;
import org.thiagocircuncisao.model.Transaction;
import org.thiagocircuncisao.presentation.*;
import org.thiagocircuncisao.repository.TransactionRepository;
import org.thiagocircuncisao.repository.impl.CurrencyRepositoryImpl;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;

@Service
public class TransactionsService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CurrencyRepositoryImpl currencyRepositoryImpl;

    public List<RetrievePurchaseResponse> getTransactionById(String id, String country, String currencyName) {
        Transaction transaction = transactionRepository.findById(id).orElseThrow(()
                -> new TransactionException("Transaction not found with id: " + id));

        List<Currency> currencies = getCurrencies(currencyRepositoryImpl.getCurrency(country, currencyName)).stream().filter(c -> c.getCountry().equals(country)
                && c.getCurrency().equals(currencyName)).toList();

        if (currencies.isEmpty())
            throw new TransactionException("Currency not found with country: " + country + " and currency: " + currencyName
                    + "or currency hasn't been updated in 6 months");

        TransactionMapper transactionMapper = new TransactionMapperImpl();
        return currencies.stream().map(currency -> {
            RetrievePurchaseResponse retrievePurchaseResponse = transactionMapper.toRetrievePurchaseResponse(transaction);
            retrievePurchaseResponse.setCurrency(currency.getCurrency());
            retrievePurchaseResponse.setCountry(currency.getCountry());
            retrievePurchaseResponse.setExchangeRate(currency.getExchangeRate());
            retrievePurchaseResponse.setAmount(Math.round((retrievePurchaseResponse.getAmount() * currency.getExchangeRate()) * 100.0) / 100.0);
            return retrievePurchaseResponse;
        }).toList();
    }

    public PurchaseResponse createTransaction(PurchaseRequest purchaseRequest) {
        if (purchaseRequest.getDescription().length() > 50)
            throw new TransactionException("Description must be less than 50 characters");

        isDateValid(purchaseRequest.getDate());

        if (purchaseRequest.getAmount() < 0)
            throw new TransactionException("Total must be greater than 0");

        if (BigDecimal.valueOf(purchaseRequest.getAmount()).scale() > 2)
            throw new TransactionException("Total must have 2 decimal places");

        TransactionMapper transactionMapper = new TransactionMapperImpl();
        Transaction transaction = transactionMapper.toTransaction(purchaseRequest);
        transactionRepository.save(transaction);
        return transactionMapper.toPurchaseResponse(transaction);
    }

    public List<CurrencyResponse> retrieveCurrencies() {
        List<Currency> currencies = getCurrencies(currencyRepositoryImpl.getCurrencies());

        CurrencyMapper currencyMapper = new CurrencyMapperImpl();
        return currencies.stream().map(currencyMapper::toCurrencyResponse).toList();
    }

    private List<Currency> getCurrencies(Map result) {
        ArrayList<Map> currencies = (ArrayList<Map>) result.get("data");
        return currencies.stream().map(currency -> Currency.builder()
                .currency((String) currency.get("currency"))
                .country((String) currency.get("country"))
                .exchangeRate(Double.valueOf((String) currency.get("exchange_rate")))
                .sourceLineNumber((String) currency.get("src_line_nbr"))
                .recordDate(LocalDate.parse((String) currency.get("record_date")))
                .build()).toList();
    }

    private void isDateValid(String dateText) {
        try {
            LocalDate.parse(dateText);
            if (LocalDate.parse(dateText).isAfter(LocalDate.now()))
                throw new TransactionException("Date must be before today");
        } catch (DateTimeException e) {
            throw new TransactionException("Date invalid be sure it is in format yyyy-MM-dd, and da and month are valid values");
        }
    }
}