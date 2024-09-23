package com.amex.service;

import com.amex.dto.entity.Billing;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BillingService {

    Billing create(Billing billing);
    List<Billing> getBillingsByStatus(Billing.Status billStatus);
}
