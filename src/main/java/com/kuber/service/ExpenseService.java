package com.kuber.service;

import com.kuber.Authentication.JwtUtils;
import com.kuber.model.Expense;
import com.kuber.model.ExpenseRequest;
import com.kuber.model.ExpenseResponse;
import com.kuber.service.mapper.ExpenseRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class ExpenseService {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static Logger LOGGER = LoggerFactory.getLogger(ExpenseService.class);

    private static final String INSERT_EXPENSE = "INSERT INTO Expense(Expense_Type, Expense_Date, Amount, Created_By, Created_Date) VALUES (:expenseType, :expenseDate, :amount, :createdBy, :createdDate)";
    private static final String GET_EXPENSE = "SELECT Expense_Id, Expense_Type, Amount, Expense_Date from Expense";
    @Transactional
    public String addExpense(ExpenseRequest expenseRequest, String token) throws SQLException {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        String userName = jwtUtils.getUserNameFromJwtToken(token);
        parameters.addValue("expenseDate", expenseRequest.getExpenseDate());
        for(Expense expense: expenseRequest.getExpenses()){
            parameters.addValue("expenseType", expense.getExpenseType());
            parameters.addValue("amount", expense.getAmount());
            parameters.addValue("createdBy", userName);
            parameters.addValue("createdDate", new Date());

            int rowsAffected = this.namedParameterJdbcTemplate.update(INSERT_EXPENSE, parameters);

            if (rowsAffected != 1) {
                throw new SQLException("Failed to insert expenses: " + expenseRequest.toString());
            }
        }
        return "Expenses added successfully";
    }

    public List<ExpenseResponse> getExpense(Map<String, String> params) throws SQLException {

        StringBuilder queryString = new StringBuilder();
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put("expenseDate", "Expense_Date");

        queryString.append(GET_EXPENSE);
        queryString.append(" WHERE 1=1 ");

        if(!params.isEmpty()){
            for (String key : params.keySet()) {
                String value = params.get(key);
                if (isNotBlank(value)) {
                        queryString.append(" AND " + columnMap.get(key) + "=:" + key);
                }
            }
        }
        queryString.append(" order by Expense_Date desc");
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource(params);
            return this.namedParameterJdbcTemplate.query(queryString.toString(), parameters, new ExpenseRowMapper());
        } catch (Exception e) {
            throw new SQLException("SQL Error ", e);
        }
    }

}
