package org.thiagocircuncisao.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.thiagocircuncisao.Application;
import org.thiagocircuncisao.exception.TransactionException;
import org.thiagocircuncisao.presentation.CurrencyResponse;
import org.thiagocircuncisao.presentation.PurchaseRequest;
import org.thiagocircuncisao.presentation.PurchaseResponse;
import org.thiagocircuncisao.presentation.RetrievePurchaseResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(  webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application.yml")
class TransactionsServiceTest {

    @Autowired
    private TransactionsService transactionsService = new TransactionsService();

    @Test
    void getTransactionById() {
        PurchaseRequest purchaseRequest = PurchaseRequest.builder().amount(1.0).description("testing")
                .date("2012-01-01").build();
        PurchaseResponse response = transactionsService.createTransaction(purchaseRequest);
        List<RetrievePurchaseResponse> retrievePurchaseResponse = transactionsService.getTransactionById(response.getId(),
                "Brazil", "Real");
        assertFalse(retrievePurchaseResponse.isEmpty());
        retrievePurchaseResponse.forEach(r -> {
            assertEquals(r.getAmount(), (double) Math.round((response.getAmount() * r.getExchangeRate()) * 100.0) / 100.0);
        });
    }

    @Test
    void getTransactionByIdWithInvalidId() {
        assertThrows(TransactionException.class, () -> {
            transactionsService.getTransactionById("123", "Brazil", "Real");
        });
    }

    @Test
    void getTransactionByIdWithInvalidCurrency() {
        PurchaseRequest purchaseRequest = PurchaseRequest.builder().amount(1.0).description("testing")
                .date("2012-01-01").build();
        PurchaseResponse response = transactionsService.createTransaction(purchaseRequest);
        assertThrows(TransactionException.class, () -> {
            transactionsService.getTransactionById(response.getId(), "Teste", "Teste");
        });
    }

    @Test
    void createTransactionSuccessfully() {
        PurchaseRequest purchaseRequest = PurchaseRequest.builder().amount(1.0).description("testing")
                .date("2012-01-01").build();
        PurchaseResponse response = transactionsService.createTransaction(purchaseRequest);
        assertEquals(response.getDescription(), purchaseRequest.getDescription());
        assertEquals(response.getAmount(), purchaseRequest.getAmount());
        assertEquals(response.getDate(), purchaseRequest.getDate());
    }

    @Test
    void createTransactionWithInvalidDescription() {
        PurchaseRequest purchaseRequest = PurchaseRequest.builder().amount(1.0)
                .description("testingtestingtestingtestingtestingtestingtestingtestingtestingtesting" +
                        "testingtestingtestingtestingtestingtestingtestingtestingtestingtestingtesting" +
                        "testingtestingtestingtestingtestingtestingtestingtestingtestingtestingtesting" +
                        "testingtestingtestingtesti")
                .date("2012-01-01").build();
        assertThrows(TransactionException.class, () -> {
            transactionsService.createTransaction(purchaseRequest);
        });
    }

    @Test
    void createTransactionWithInvalidAmount() {
        PurchaseRequest purchaseRequest = PurchaseRequest.builder().amount(-1.0).description("testing")
                .date("2012-01-01").build();
        assertThrows(TransactionException.class, () -> {
            transactionsService.createTransaction(purchaseRequest);
        });
    }

    @Test
    void createTransactionWithInvalidAmountDecimalPlaces() {
        PurchaseRequest purchaseRequest = PurchaseRequest.builder().amount(1.123).description("testing")
                .date("2012-01-01").build();
        assertThrows(TransactionException.class, () -> {
            transactionsService.createTransaction(purchaseRequest);
        });
    }

    @Test
    void createTransactionWithInvalidDate() {
        PurchaseRequest purchaseRequest = PurchaseRequest.builder().amount(1.0).description("testing")
                .date("2030-01-01").build();
        assertThrows(TransactionException.class, () -> {
            transactionsService.createTransaction(purchaseRequest);
        });
    }

    @Test
    void retrieveCurrencies() {
        List<CurrencyResponse> currencies = transactionsService.retrieveCurrencies();
        assertFalse(currencies.isEmpty());
    }
}