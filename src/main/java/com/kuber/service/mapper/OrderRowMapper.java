package com.kuber.service.mapper;

import com.kuber.model.Orders;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderRowMapper implements RowMapper<Orders> {
    @Override
    public Orders mapRow(ResultSet resultSet, int i) throws SQLException {
        Orders order = new Orders();

        order.setOrderId(resultSet.getInt("Order_Id"));
        order.setCustomerName(resultSet.getString("Customer_Name"));
        order.setOrderDate(resultSet.getTimestamp("Date"));

        return order;
    }
    
}
