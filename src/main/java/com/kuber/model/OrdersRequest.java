package com.kuber.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class OrdersRequest {
    private int orderId;

    private String customerName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;

    private int totalAmount;

    private String billNo;

    private boolean assignUpdate;

    private String assigneeName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedDate;

    private boolean assignedStatus;

    private List<OrderDetailsDictionary> orders;

}
