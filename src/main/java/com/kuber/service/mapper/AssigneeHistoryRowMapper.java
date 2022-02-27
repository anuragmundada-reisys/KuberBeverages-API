package com.kuber.service.mapper;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kuber.model.OrderDetails;
import com.kuber.model.OrderResponse;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AssigneeHistoryRowMapper implements RowMapper<AssignHistoryResponse> {
    @Override
    public AssignHistoryResponse mapRow(ResultSet resultSet, int i) throws SQLException {
        AssignHistoryResponse assignHistoryResponse = new AssignHistoryResponse();

        assignHistoryResponse.setOrderId(resultSet.getInt("Order_Id"));
        assignHistoryResponse.setBillNo(resultSet.getString("Bill_No"));
        assignHistoryResponse.setAssignedStatus(resultSet.getBoolean("Assigned_Status"));
        assignHistoryResponse.setAssigneeName(resultSet.getString("Assignee_Name"));
        assignHistoryResponse.setUpdatedDate(resultSet.getTimestamp("Updated_Date"));

        return assignHistoryResponse;
    }
}
