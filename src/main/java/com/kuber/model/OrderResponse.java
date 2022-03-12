package com.kuber.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
public class OrderResponse {

    private int orderId;

    private String customerName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;

    private int totalAmount;

    private String billNo;

    private int balanceDue;

    private boolean assignedStatus;

    private String assigneeName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date assignedUpdatedDate;

    private ArrayList<OrderDetails> orders;
}
