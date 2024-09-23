package com.amex.repository;

import com.amex.dto.entity.Billing;
import com.amex.dto.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillingRepository extends JpaRepository<Billing, Long> {

    List<Billing> findBillingByStatus(Billing.Status status);
}
