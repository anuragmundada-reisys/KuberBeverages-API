package com.kuber.service;

import com.kuber.model.*;
import com.kuber.service.mapper.OrderResponseRowMapper;
import com.kuber.service.mapper.OrderRowMapper;
import com.kuber.service.mapper.RawMaterialDictionaryRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdersService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static Logger LOGGER = LoggerFactory.getLogger(OrdersService.class);

    private static final String INSERT_ORDER = "INSERT INTO Orders(Customer_Name, Status, TotalAmount, Date) VALUES ( :customerName, :status, :totalAmount, :orderDate)";
    private static final String INSERT_ORDER_DETAILS = "INSERT INTO Order_Details(Order_Id, Product_Id, Quantity, Rate, Amount) VALUES ( :orderId, :productId, :quantity, :rate, :amount)";
    private static final String GET_LAST_INSERTED_ORDER_ID = "SELECT LAST_INSERT_ID()";

    private static final String GET_ORDERS = "Select orders.*, JSON_ARRAYAGG(JSON_OBJECT('orderDetailsId', od.Order_Details_Id, 'productId', od.Product_Id , 'quantity', od.Quantity, 'rate', od.Rate, 'productType', p.Product_Type, 'amount', od.Amount )) as orders " +
            "from Orders orders inner join Order_Details od on orders.Order_Id = od.Order_Id left join Product p on p.Product_Id = od.Product_Id group by orders.Order_Id order by orders.Order_Id desc;";

    private static final String UPDATE_ORDER = "UPDATE Orders set Status=:status, TotalAmount=:totalAmount where Order_Id=:orderId";
    private static final String UPDATE_ORDER_DETAILS = "UPDATE Order_Details set Product_Id=:productId, Quantity=:quantity, Rate=:rate, Amount=:amount where Order_Details_Id=:orderDetailsId";
    private static final String GET_ORDER_BY_ID = "SELECT count(*) FROM Orders where Order_Id=:orderId";


    @Transactional
    public String addOrder(OrdersRequest orderRequest) throws SQLException {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(orderRequest);
        int rowsAffected = this.namedParameterJdbcTemplate.update(INSERT_ORDER, namedParameters);
        if (rowsAffected != 1) {
            throw new SQLException("Failed to insert Order: " + orderRequest.toString());
        }
        int lastInsertedOrderId = this.namedParameterJdbcTemplate.queryForObject(GET_LAST_INSERTED_ORDER_ID, namedParameters,Integer.class);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("orderId", lastInsertedOrderId);
        for (OrderDetailsDictionary orderDetails: orderRequest.getOrders()) {
            parameters.addValue("productId", orderDetails.getProductId());
            parameters.addValue("quantity", orderDetails.getQuantity());
            parameters.addValue("rate", orderDetails.getRate());
            parameters.addValue("amount", orderDetails.getAmount());

            int orderDetailsRowsAffected = this.namedParameterJdbcTemplate.update(INSERT_ORDER_DETAILS, parameters);
            if (orderDetailsRowsAffected != 1) {
                throw new SQLException("Failed to insert Order: " + orderRequest.toString());
            }
        }
        return "Order successfully added";
    }

    @Transactional
    public String updateOrder(OrdersRequest orderRequest) throws SQLException {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(orderRequest);
        int count = this.namedParameterJdbcTemplate.queryForObject(GET_ORDER_BY_ID, namedParameters, Integer.class);
        if( count == 0){
            throw new NotFoundException("Order Not Found");
        }
        int rowsAffected = this.namedParameterJdbcTemplate.update(UPDATE_ORDER, namedParameters);

        if (rowsAffected != 1) {
            throw new SQLException("Failed to update Order: " + orderRequest.toString());
        }
        for(OrderDetailsDictionary orderDetailsDictionary: orderRequest.getOrders()){
            SqlParameterSource parameters = new BeanPropertySqlParameterSource(orderDetailsDictionary);
            this.namedParameterJdbcTemplate.update(UPDATE_ORDER_DETAILS, parameters);
        }
        return "Order successfully updated";
    }

    public List<OrderResponse> getOrders() throws SQLException {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            return this.namedParameterJdbcTemplate.query(GET_ORDERS, parameters, new OrderResponseRowMapper());
        } catch (Exception e) {
            throw new SQLException("SQL Error ", e);
        }
    }
}
