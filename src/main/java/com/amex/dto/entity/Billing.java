package com.amex.dto.entity;

import com.amex.exception.BillingServiceException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;
    private Long amount;
    private Status status;


    @Getter
    public static enum Status {
        PAID("paid", 1), DUE("due", 0);

        private final String name;
        private final int value;

        Status(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public static Status fromName(String name) throws BillingServiceException {
            return Arrays.stream(Status.values())
                    .filter(statusEnum -> StringUtils.equalsIgnoreCase(statusEnum.getName(), name))
                    .findFirst()
                    .orElseThrow(() -> new BillingServiceException("Status value not acceptable##101"));
        }


    }
}
