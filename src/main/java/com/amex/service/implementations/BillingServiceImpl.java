package com.amex.service.implementations;

import com.amex.dto.entity.Billing;
import com.amex.repository.BillingRepository;
import com.amex.service.BillingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BillingServiceImpl implements BillingService {

    private final BillingRepository billingRepository;


    @Autowired
    public BillingServiceImpl(BillingRepository billingRepository) {
        this.billingRepository = billingRepository;
    }

    @Override
    public Billing create(Billing billing) {
        return billingRepository.save(billing);
    }

    @Override
    public List<Billing> getBillingsByStatus(Billing.Status billStatus) {
        return billingRepository.findBillingByStatus(billStatus);
    }
}
