package com.kuber.service.mapper;

import com.kuber.model.ExpenseByDateMetricsResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpenseByDateRowMapper implements RowMapper<ExpenseByDateMetricsResponse> {
    @Override
    public ExpenseByDateMetricsResponse mapRow(ResultSet resultSet, int i) throws SQLException {
        ExpenseByDateMetricsResponse expenseByDateMetricsResponse = new ExpenseByDateMetricsResponse();

        expenseByDateMetricsResponse.setTotalExpenseByDate(resultSet.getInt("Total_Expense"));
        return expenseByDateMetricsResponse;
    }
}
