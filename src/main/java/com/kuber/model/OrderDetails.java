package com.kuber.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderDetails {

    private int orderDetailsId;

    private int productId;

    private int orderId;

    private int quantity;

    private int rate;

    private int amount;

    private String productType;
}
