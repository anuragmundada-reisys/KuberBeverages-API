package com.kuber.service;

import com.kuber.model.OrderDetailsDictionary;
import com.kuber.model.OrderResponse;
import com.kuber.model.OrdersRequest;
import com.kuber.model.AssignHistoryResponse;
import com.kuber.service.mapper.AssigneeHistoryRowMapper;
import com.kuber.service.mapper.OrderResponseRowMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.parseBoolean;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class OrdersService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static Logger LOGGER = LoggerFactory.getLogger(OrdersService.class);

    private static final String INSERT_ORDER = "INSERT INTO Orders(Customer_Name, TotalAmount, Date, Bill_No) VALUES ( :customerName, :totalAmount, :orderDate, :billNo)";
    private static final String INSERT_ORDER_DETAILS = "INSERT INTO Order_Details(Order_Id, Product_Id, Quantity, Rate, Amount) VALUES ( :orderId, :productId, :quantity, :rate, :amount)";
    private static final String GET_LAST_INSERTED_ORDER_ID = "SELECT LAST_INSERT_ID()";


    private static final String UPDATE_ORDER = "UPDATE Orders set TotalAmount=:totalAmount where Order_Id=:orderId";
    private static final String UPDATE_ORDER_DETAILS = "UPDATE Order_Details set Product_Id=:productId, Quantity=:quantity, Rate=:rate, Amount=:amount where Order_Details_Id=:orderDetailsId";
    private static final String GET_ORDER_BY_ID = "SELECT count(*) FROM Orders where Order_Id=:orderId";
    private static final String ASSIGN_ORDER = "UPDATE Orders set Assignee_Name=:assigneeName, Updated_Date=:updatedDate, Assigned_Status=:assignedStatus where Order_Id=:orderId";
    private static final String GET_ORDER_ASSIGNEE_HISTORY = "SELECT * FROM Assignee_History where Order_Id=:orderId order by Order_Id desc";

    private String GET_ORDERS = "Select orders.*, orders.TotalAmount - sum(case when ph.totalReceived is null then 0 else ph.totalReceived end) as Balance_Due, od.orders\n" +
            " from Orders orders\n" +
            " left join (SELECT CASE WHEN sum(Received_Payment) IS NULL THEN 0 ELSE sum(Received_Payment) END AS totalReceived, Order_Id \n" +
            " FROM Payment_History GROUP BY Order_Id) ph on orders.Order_Id = ph.Order_Id\n" +
            " left join (SELECT Order_Id, JSON_ARRAYAGG(JSON_OBJECT('orderDetailsId', o.Order_Details_Id, 'productId', p.Product_Id , 'quantity', o.Quantity, 'rate', o.Rate, 'productType', p.Product_Type, 'amount', o.Amount )) as orders FROM Order_Details o left join Product p on p.Product_Id = o.Product_Id  GROUP BY o.Order_Id) od \n" +
            " on orders.Order_Id = od.Order_Id";

    @Transactional
    public String addOrder(OrdersRequest orderRequest) throws SQLException, Exception {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(orderRequest);
        int rowsAffected = this.namedParameterJdbcTemplate.update(INSERT_ORDER, namedParameters);
        if (rowsAffected != 1) {
            throw new SQLException("Failed to insert Order: " + orderRequest.toString());
        }
        int lastInsertedOrderId = this.namedParameterJdbcTemplate.queryForObject(GET_LAST_INSERTED_ORDER_ID, namedParameters, Integer.class);
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("orderId", lastInsertedOrderId);
        for (OrderDetailsDictionary orderDetails : orderRequest.getOrders()) {
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
        if (count == 0) {
            throw new NotFoundException("Order Not Found");
        }
        if (orderRequest.isAssignUpdate()) {
            int rowsAffected = this.namedParameterJdbcTemplate.update(ASSIGN_ORDER, namedParameters);

            if (rowsAffected != 1) {
                throw new SQLException("Failed to assign Order: " + orderRequest.toString());
            }
            return "Order Assigned Successfully";
        } else {
            int rowsAffected = this.namedParameterJdbcTemplate.update(UPDATE_ORDER, namedParameters);

            if (rowsAffected != 1) {
                throw new SQLException("Failed to update Order: " + orderRequest.toString());
            }
            for (OrderDetailsDictionary orderDetailsDictionary : orderRequest.getOrders()) {
                SqlParameterSource parameters = new BeanPropertySqlParameterSource(orderDetailsDictionary);
                this.namedParameterJdbcTemplate.update(UPDATE_ORDER_DETAILS, parameters);
            }
            return "Order successfully updated";
        }

    }

    public List<OrderResponse> getOrders(Map<String, String> params) throws SQLException {

            StringBuilder queryString = new StringBuilder();
            Map<String, String> columnMap = new HashMap<>();
            columnMap.put("billNo", "Bill_No");
            columnMap.put("customerName", "Customer_Name");
            columnMap.put("orderDate", "Date");
            columnMap.put("assignedStatus", "Assigned_Status");
            columnMap.put("assigneeName", "Assignee_Name");
            columnMap.put("updatedDate", "Updated_Date");

            queryString.append(GET_ORDERS);
            queryString.append(" WHERE 1=1 ");
            if(!params.isEmpty()){
                for (String key : params.keySet()) {
                    String value = params.get(key);
                    if (!key.equalsIgnoreCase("orderStatus") && isNotBlank(value)) {
                        queryString.append(" AND " + columnMap.get(key) + "=:" + key);
                    }
                }
            }

            queryString.append(" group by orders.Order_Id order by orders.Order_Id desc");
            try {
                MapSqlParameterSource parameters = new MapSqlParameterSource(params);
                parameters.addValue("assignedStatus", parseBoolean(params.get("assignedStatus")));
                return this.namedParameterJdbcTemplate.query(queryString.toString(), parameters, new OrderResponseRowMapper());
            } catch (Exception e) {
                throw new SQLException("SQL Error ", e);
            }

    }

    public List<AssignHistoryResponse> getOrderAssigneeHistory(int orderId) throws SQLException {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("orderId", orderId);
            return this.namedParameterJdbcTemplate.query(GET_ORDER_ASSIGNEE_HISTORY, parameters, new AssigneeHistoryRowMapper());
        } catch (Exception e) {
            throw new SQLException("SQL Error ", e);
        }
    }
}

