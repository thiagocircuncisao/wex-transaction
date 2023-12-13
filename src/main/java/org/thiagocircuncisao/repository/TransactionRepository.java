package org.thiagocircuncisao.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.thiagocircuncisao.model.Transaction;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
}
