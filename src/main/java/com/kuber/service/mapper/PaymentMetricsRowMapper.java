package com.kuber.service.mapper;

import com.kuber.model.MetricsResponse;
import com.kuber.model.PaymentMetricsResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PaymentMetricsRowMapper implements RowMapper<PaymentMetricsResponse> {
    @Override
    public PaymentMetricsResponse mapRow(ResultSet resultSet, int i) throws SQLException {
        PaymentMetricsResponse paymentMetricsResponse = new PaymentMetricsResponse();

        paymentMetricsResponse.setPaymentMode(resultSet.getString("Payment_Mode"));
        paymentMetricsResponse.setTotalAmount(resultSet.getInt("Total_Amount"));
        return paymentMetricsResponse;
    }

}
