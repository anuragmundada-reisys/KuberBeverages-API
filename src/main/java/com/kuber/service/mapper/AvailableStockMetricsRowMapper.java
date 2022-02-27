package com.kuber.service.mapper;

import com.kuber.model.AvailableStockMetricsResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AvailableStockMetricsRowMapper implements RowMapper<AvailableStockMetricsResponse> {
    @Override
    public AvailableStockMetricsResponse mapRow(ResultSet resultSet, int i) throws SQLException {
        AvailableStockMetricsResponse availableStockMetricsResponse = new AvailableStockMetricsResponse();

        availableStockMetricsResponse.setAvailableStock(resultSet.getInt("availableStock"));
        availableStockMetricsResponse.setTitle(resultSet.getString("title"));
        return availableStockMetricsResponse;
    }
}
