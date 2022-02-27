package com.kuber.service;

import com.kuber.model.*;
import com.kuber.service.mapper.OrderResponseRowMapper;
import com.kuber.service.mapper.PaymentHistoryRowMapper;
import com.kuber.service.mapper.RawMaterialsRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentHistoryService {

    private static Logger LOGGER = LoggerFactory.getLogger(PaymentHistoryService.class);

    private static final String INSERT_PAYMENT_HISTORY = "INSERT INTO Payment_History(Order_Id, Received_Payment, Received_Date, Payment_Mode, Receiver_Name) VALUES (:orderId, :receivedPayment, :receivedPaymentDate, :paymentMode, :receiverName)";

    private static final String GET_PAYMENT_HISTORY_DETAILS = "SELECT * FROM Payment_History WHERE Order_Id=:orderId";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Transactional
    public String addPaymentHistory(PaymentHistoryRequest paymentHistoryRequest) throws SQLException {

        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("orderId", paymentHistoryRequest.getOrderId());
        for (PaymentHistory paymentHistory : paymentHistoryRequest.getReceivedPayments()) {
            parameters.addValue("receivedPayment", paymentHistory.getReceivedAmount());
            parameters.addValue("receivedPaymentDate", paymentHistory.getReceivedPaymentDate());
            parameters.addValue("paymentMode", paymentHistory.getPaymentMode());
            parameters.addValue("receiverName", paymentHistory.getReceiverName());

            int paymentHistoryRowsAffected = this.namedParameterJdbcTemplate.update(INSERT_PAYMENT_HISTORY, parameters);
            if (paymentHistoryRowsAffected != 1) {
                throw new SQLException("Failed to insert Payment History: " + paymentHistoryRequest.toString());
            }
        }
        return "Payment successfully added";
    }

    public List<PaymentHistory> getPaymentHistoryByOrderId(int orderId) throws SQLException {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("orderId", orderId);
            return this.namedParameterJdbcTemplate.query(GET_PAYMENT_HISTORY_DETAILS, parameters, new PaymentHistoryRowMapper());
        } catch (Exception e) {
            throw new SQLException("SQL Error ", e);
        }
    }
}


