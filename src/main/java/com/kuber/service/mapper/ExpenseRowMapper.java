package com.kuber.service.mapper;

import com.kuber.model.ExpenseResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExpenseRowMapper implements RowMapper<ExpenseResponse> {

    @Override
    public ExpenseResponse mapRow(ResultSet resultSet, int i) throws SQLException {
        ExpenseResponse expenseResponse = new ExpenseResponse();

        expenseResponse.setExpenseId(resultSet.getInt("Expense_Id"));
        expenseResponse.setExpenseType(resultSet.getString("Expense_Type"));
        expenseResponse.setExpenseDate(resultSet.getTimestamp("Expense_Date"));
        expenseResponse.setAmount(resultSet.getInt("Amount"));

        return expenseResponse;
    }
}
