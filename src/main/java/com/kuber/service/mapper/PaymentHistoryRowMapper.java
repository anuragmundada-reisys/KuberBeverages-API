package com.kuber.service.mapper;

import com.kuber.model.PaymentHistory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentHistoryRowMapper implements RowMapper<PaymentHistory> {

    public PaymentHistory mapRow(ResultSet resultSet, int i) throws SQLException {
        PaymentHistory paymentHistory = new PaymentHistory();

        paymentHistory.setPaymentId(resultSet.getInt("Payment_Id"));
        paymentHistory.setOrderId(resultSet.getInt("Order_Id"));
        paymentHistory.setReceivedAmount(resultSet.getInt("Received_Payment"));
        paymentHistory.setReceivedPaymentDate(resultSet.getTimestamp("Received_Date"));
        paymentHistory.setPaymentMode(resultSet.getString("Payment_Mode"));
        paymentHistory.setReceiverName(resultSet.getString("Receiver_Name"));

        return paymentHistory;
    }
}
