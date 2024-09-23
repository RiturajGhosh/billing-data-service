package com.amex.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class BillingServiceException extends RuntimeException {
    private String message;

    public BillingServiceException(String message) {
        super(message);
    }
}
