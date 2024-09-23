package com.amex.mapper;

import com.amex.dto.entity.Billing;
import com.amex.dto.entity.Customer;
import com.amex.dto.request.CreateBillingRequest;
import com.amex.dto.response.BillingResponse;
import com.amex.exception.BillingServiceException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BillingMapper {

    @Mapping(source = "status", target = "status", qualifiedByName = "mapStatus")
    Billing toBilling(CreateBillingRequest billingRequest);

    BillingResponse toBillingResponse(Billing billing);

    List<BillingResponse> toBillingResponseList(List<Billing> billingList);

    @Named("mapStatus")
    default Billing.Status mapStatus(String name) throws BillingServiceException {
        return Billing.Status.fromName(name);
    }
}
