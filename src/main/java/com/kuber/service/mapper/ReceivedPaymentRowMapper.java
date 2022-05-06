package com.kuber.service.mapper;

import com.kuber.model.PaymentHistory;
import com.kuber.model.ReceivedPayment;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ReceivedPaymentRowMapper implements RowMapper<ReceivedPayment> {

    public ReceivedPayment mapRow(ResultSet resultSet, int i) throws SQLException {
        ReceivedPayment receivedPayment = new ReceivedPayment();

        receivedPayment.setBillNo(resultSet.getString("Bill_No"));
        receivedPayment.setCustomerName(resultSet.getString("Customer_Name"));
        receivedPayment.setReceivedAmount(resultSet.getInt("Received_Payment"));
        receivedPayment.setReceivedPaymentDate(resultSet.getTimestamp("Received_Date"));
        receivedPayment.setPaymentMode(resultSet.getString("Payment_Mode"));
        receivedPayment.setReceiverName(resultSet.getString("Receiver_Name"));

        return receivedPayment;
    }
}
