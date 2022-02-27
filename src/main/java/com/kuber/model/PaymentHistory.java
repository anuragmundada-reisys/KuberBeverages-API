package com.kuber.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class PaymentHistory {
    private int paymentId;

    private int orderId;

    private int receivedAmount;

    private  String paymentMode;

    private String receiverName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date receivedPaymentDate;

}
