package com.kuber.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaymentHistoryRequest {
    private int orderId;

    private List<PaymentHistory> receivedPayments;
}
