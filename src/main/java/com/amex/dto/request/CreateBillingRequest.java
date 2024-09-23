package com.amex.dto.request;

import com.amex.dto.entity.Billing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBillingRequest {
    private Long amount;
    private String status;
}
