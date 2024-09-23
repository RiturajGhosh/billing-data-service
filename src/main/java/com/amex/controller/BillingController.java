package com.amex.controller;

import com.amex.dto.entity.Billing;
import com.amex.dto.request.CreateBillingRequest;
import com.amex.dto.response.BillingResponse;
import com.amex.mapper.BillingMapper;
import com.amex.service.BillingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/billing")
@Slf4j
public class BillingController {

    private final BillingService billingService;
    private final BillingMapper mapper;

    @Autowired
    public BillingController(BillingService billingService, BillingMapper mapper) {
        this.billingService = billingService;
        this.mapper = mapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BillingResponse createBilling(@RequestBody CreateBillingRequest billingRequest) {
        log.info("Http request:: path: {}, request: {}, method: {}", "/v1/billing", billingRequest, HttpMethod.POST);
        return mapper.toBillingResponse(billingService
                .create(mapper.toBilling(billingRequest)));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<BillingResponse> getBillingsByBillStatus(@RequestBody StatusRequest billStatus) throws Exception {
        log.info("Http request:: path: {}, request: {}, method: {}", "/v1/billing", billStatus, HttpMethod.GET);
        return mapper.toBillingResponseList(billingService
                .getBillingsByStatus(Billing.Status.fromName(billStatus.getStatus())));
    }
}
