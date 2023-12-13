package org.thiagocircuncisao.mapper;

import org.mapstruct.Mapper;
import org.thiagocircuncisao.model.Transaction;
import org.thiagocircuncisao.presentation.PurchaseRequest;
import org.thiagocircuncisao.presentation.PurchaseResponse;
import org.thiagocircuncisao.presentation.RetrievePurchaseResponse;

@Mapper
public interface TransactionMapper {
    Transaction toTransaction(PurchaseRequest purchaseRequest);
    PurchaseResponse toPurchaseResponse(Transaction transaction);
    RetrievePurchaseResponse toRetrievePurchaseResponse(Transaction transaction);
}
