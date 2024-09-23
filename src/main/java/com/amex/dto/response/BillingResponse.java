package com.amex.dto.response;

import com.amex.dto.entity.Billing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillingResponse {
    private Long billId;
    private Long amount;
    private Billing.Status status;
}
