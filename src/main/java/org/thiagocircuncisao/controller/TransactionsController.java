package org.thiagocircuncisao.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.thiagocircuncisao.exception.TransactionException;
import org.thiagocircuncisao.presentation.CurrencyResponse;
import org.thiagocircuncisao.presentation.PurchaseRequest;
import org.thiagocircuncisao.presentation.PurchaseResponse;
import org.thiagocircuncisao.presentation.RetrievePurchaseResponse;
import org.thiagocircuncisao.service.TransactionsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TransactionsController {
    @Autowired
    private TransactionsService transactionsService;

    @RequestMapping(value = "/create-purchase", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<PurchaseResponse> purchase(@RequestBody PurchaseRequest purchaseRequest) {
        return ResponseEntity.ok(transactionsService.createTransaction(purchaseRequest));
    }

    @RequestMapping(value = "/retrieve-purchase/{id}/{country}/{currency}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<RetrievePurchaseResponse>> retrievePurchase(@PathVariable String id, @PathVariable String country,
                                                                     @PathVariable String currency) {
        if (country == null || country.isEmpty()) {
            throw new TransactionException("Country must be provided");
        }

        if (currency == null || currency.isEmpty()) {
            throw new TransactionException("Currency must be provided");
        }

        return ResponseEntity.ok(transactionsService.getTransactionById(id, country, currency));
    }

    @RequestMapping(value = "/retrieve-currencies", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<CurrencyResponse>> retrieveCurrencies() {
        return ResponseEntity.ok(transactionsService.retrieveCurrencies());
    }
}
