package com.kuber.service;

import com.kuber.model.PaymentMetricsResponse;
import com.kuber.service.mapper.PaymentMetricsRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class PaymentMetricsService {
    private static Logger LOGGER = LoggerFactory.getLogger(PaymentMetricsService.class);

    private static final String GET_PAYMENT_METRICS = "SELECT sum(Received_Payment) as Total_Amount, Payment_Mode from Payment_History  where DATE_FORMAT(Received_Date, '%Y-%m-%d')= :receivedDate group by Payment_Mode";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<PaymentMetricsResponse> getPaymentMetrics(Date receivedDate) throws SQLException {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            parameters.addValue("receivedDate", formatter.format(receivedDate));
            return this.namedParameterJdbcTemplate.query(GET_PAYMENT_METRICS, parameters, new PaymentMetricsRowMapper());

        } catch (Exception e) {
            throw new SQLException("SQL Error ", e);
        }
    }

}
