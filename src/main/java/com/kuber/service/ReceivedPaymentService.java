package com.kuber.service;

import com.kuber.model.OrderResponse;
import com.kuber.model.ReceivedPayment;
import com.kuber.service.mapper.OrderResponseRowMapper;
import com.kuber.service.mapper.ReceivedPaymentRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Boolean.parseBoolean;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class ReceivedPaymentService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static Logger LOGGER = LoggerFactory.getLogger(OrdersService.class);

    private static final String GET_RECEIVED_PAYMENT = "Select o.Bill_No, o.Customer_Name, ph.Received_Payment, ph.Received_Date, ph.Payment_Mode, ph.Receiver_Name from Orders o inner join Payment_History ph  on o.Order_Id = ph.Order_Id ";

    public List<ReceivedPayment> getReceivedPayment(Map<String, String> params) throws SQLException {

        StringBuilder queryString = new StringBuilder();
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put("billNo", "Bill_No");
        columnMap.put("customerName", "Customer_Name");
        columnMap.put("assigneeName", "Receiver_Name");
        columnMap.put("receivedPaymentDate", "Received_Date");

        queryString.append(GET_RECEIVED_PAYMENT);
        queryString.append(" WHERE 1=1 ");
        if(!params.isEmpty()){
            for (String key : params.keySet()) {
                String value = params.get(key);
                if ( isNotBlank(value)) {
                    queryString.append(" AND " + columnMap.get(key) + "=:" + key);
                }
            }
        }

        queryString.append(" order by ph.Received_Date desc");
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource(params);
            return this.namedParameterJdbcTemplate.query(queryString.toString(), parameters, new ReceivedPaymentRowMapper());
        } catch (Exception e) {
            throw new SQLException("SQL Error ", e);
        }

    }
}
