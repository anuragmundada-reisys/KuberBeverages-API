package com.kuber.utility;

import com.kuber.model.InventoryRequest;
import com.kuber.model.OrdersRequest;
import com.kuber.model.RawMaterialPurchaseRequest;

import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static List<String> validateRawMaterialsPurchaseRequest(RawMaterialPurchaseRequest rawMaterialPurchaseRequest) {
        List<String> errors = new ArrayList<>();

        if (rawMaterialPurchaseRequest.getRawMaterialId() == 0) {
            errors.add("Raw Material Id is required");
        }
        if (rawMaterialPurchaseRequest.getProductId() == 0) {
            errors.add("Product Id is required");
        }
        if (rawMaterialPurchaseRequest.getQuantity() == 0) {
            errors.add("Quantity cannot be 0");
        }
        return errors;
    }

    public static List<String> validateOrderRequest(OrdersRequest orderRequest) {
        List<String> errors = new ArrayList<>();

        if (orderRequest.getOrders().isEmpty()) {
            errors.add("At least one order is required");
        }
        return errors;
    }

    public static List<String> validateUpdateOrderRequest(OrdersRequest orderRequest) {
        List<String> errors = new ArrayList<>();

        if (orderRequest.getOrderId() == 0) {
            errors.add("Order Id is required");
        }
        return errors;
    }

    public static List<String> validateInventoryRequest(InventoryRequest inventoryRequest) {
        List<String> errors = new ArrayList<>();

        if (inventoryRequest.getProductId() == 0) {
            errors.add("Product Id is required");
        }
        if (inventoryRequest.getQuantity() == 0) {
            errors.add("Quantity cannot be 0");
        }
        return errors;
    }
}
