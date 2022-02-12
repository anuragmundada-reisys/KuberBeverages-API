package com.kuber.service.mapper;

import com.kuber.model.OrderDetails;
import com.kuber.model.Orders;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderDetailsRowMapper implements RowMapper<OrderDetails> {
    @Override
    public OrderDetails mapRow(ResultSet resultSet, int i) throws SQLException {
        OrderDetails orderDetails = new OrderDetails();

        orderDetails.setOrderDetailsId(resultSet.getInt("Order_Details_Id"));
        orderDetails.setOrderId(resultSet.getInt("Order_Id"));
        orderDetails.setProductId(resultSet.getInt("Product_Id"));
        orderDetails.setQuantity(resultSet.getInt("Quantity"));
        orderDetails.setRate(resultSet.getInt("Rate"));
        return orderDetails;
    }

}
