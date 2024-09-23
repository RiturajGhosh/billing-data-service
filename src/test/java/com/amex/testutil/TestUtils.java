package com.amex.testutil;

import com.amex.dto.entity.Billing;
import com.amex.dto.request.CreateBillingRequest;

public class TestUtils {

    public static Billing mockBillingRequest(){
        var bill = new Billing();
        bill.setAmount(12313L);
        bill.setStatus(Billing.Status.PAID);
        bill.setBillId(123L);
        return bill;
    }
}
