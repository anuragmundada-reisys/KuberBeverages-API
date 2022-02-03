package com.kuber.service.mapper;

import com.kuber.model.MetricsResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MetricsResponseRowMapper implements RowMapper<MetricsResponse> {

        @Override
        public MetricsResponse mapRow(ResultSet resultSet, int i) throws SQLException {
            MetricsResponse metricsResponse = new MetricsResponse();

            metricsResponse.setProductId(resultSet.getInt("productId"));
            metricsResponse.setTitle(resultSet.getString("title"));
            metricsResponse.setPendingOrders(resultSet.getInt("pendingOrders"));
            metricsResponse.setAvailableStock(resultSet.getInt("availableStock"));

            return metricsResponse;
        }

}
