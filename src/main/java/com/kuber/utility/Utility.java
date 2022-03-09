package com.kuber.utility;

import com.kuber.model.*;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

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
            errors.add("Please add Ordered items");
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

        if(isEmpty(inventoryRequest.getProducts())){
            errors.add("At least one Product is required");
        }
        return errors;
    }

    public static List<String> validateSignUpRequest(SignupRequest signupRequest) {
        List<String> errors = new ArrayList<>();

        if (signupRequest.getUserName().isEmpty() || signupRequest.getEmail().isEmpty() || signupRequest.getPassword().isEmpty()) {
            errors.add("All Fields are required!!");
        }

        return errors;
    }

    public static List<String> validateLoginRequest(LoginRequest loginRequest) {
        List<String> errors = new ArrayList<>();

        if (loginRequest.getUsername().isEmpty() ||  loginRequest.getPassword().isEmpty()) {
            errors.add("All Fields are required!!");
        }

        return errors;
    }
}
