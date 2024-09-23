package com.amex.service.implementations;

import com.amex.dto.entity.Billing;
import com.amex.repository.BillingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static com.amex.testutil.TestUtils.mockBillingRequest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
class BillingServiceImplTest {

    @Mock
    private BillingRepository repository;
    @InjectMocks
    private BillingServiceImpl billingService;

    @Test
    void create() {
        var billRequest = mockBillingRequest();
        Mockito.doReturn(new Billing()).when(repository).save(any());
        assertNotNull(billingService.create(billRequest));
    }

    @Test
    void getBillingsByStatus() {
        Mockito.doReturn(new ArrayList<>()).when(repository).findBillingByStatus(any());
        assertNotNull(billingService.getBillingsByStatus(Billing.Status.DUE));
    }
}