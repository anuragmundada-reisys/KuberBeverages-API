package com.kuber.service;

import com.kuber.model.Orders;
import com.kuber.model.OrdersRequest;
import com.kuber.service.mapper.OrderRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
public class OrdersService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static Logger LOGGER = LoggerFactory.getLogger(OrdersService.class);

    private static final String INSERT_ORDER = "INSERT INTO Orders(Product_Id, Customer_Name, Quantity, Status, Date) VALUES (:productId, :customerName, :quantity, :status, :orderDate)";
    private static final String GET_ORDERS = "SELECT orders.*, p.Product_Type FROM Orders orders LEFT JOIN  Product p ON orders.Product_Id = p.Product_Id order by Order_Id desc";


    @Transactional
    public String addOrder(OrdersRequest orderRequest) throws SQLException {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(orderRequest);
        int rowsAffected = this.namedParameterJdbcTemplate.update(INSERT_ORDER, namedParameters);

        if (rowsAffected != 1) {
            throw new SQLException("Failed to insert Order: " + orderRequest.toString());
        }
        return "Order successfully added";
    }

    public List<Orders> getOrders() throws SQLException {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            return this.namedParameterJdbcTemplate.query(GET_ORDERS, parameters, new OrderRowMapper());
        } catch (Exception e) {
            throw new SQLException("SQL Error ", e);
        }
    }
}
