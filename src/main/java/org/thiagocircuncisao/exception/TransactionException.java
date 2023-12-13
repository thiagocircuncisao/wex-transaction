package org.thiagocircuncisao.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TransactionException extends RuntimeException {
    private String message;
}
