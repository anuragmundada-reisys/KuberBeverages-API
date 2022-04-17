package com.kuber.service.mapper;

import com.kuber.model.TotalBalanceDueMetricsResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class TotalBalanceDueRowMapper implements RowMapper<TotalBalanceDueMetricsResponse> {
    @Override
    public TotalBalanceDueMetricsResponse mapRow(ResultSet resultSet, int i) throws SQLException {
        TotalBalanceDueMetricsResponse totalBalanceDueMetricsResponse = new TotalBalanceDueMetricsResponse();

        totalBalanceDueMetricsResponse.setTotalBalanceDue(resultSet.getInt("Total_Balance_Due"));
        return totalBalanceDueMetricsResponse;
    }
}
