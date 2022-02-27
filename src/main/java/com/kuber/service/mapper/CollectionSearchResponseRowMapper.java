package com.kuber.service.mapper;

import com.kuber.model.CollectionSearchResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CollectionSearchResponseRowMapper implements RowMapper<CollectionSearchResponse> {

    @Override
    public CollectionSearchResponse mapRow(ResultSet resultSet, int i) throws SQLException {
        CollectionSearchResponse collectionSearchResponse = new CollectionSearchResponse();

        collectionSearchResponse.setOrderId(resultSet.getInt("Order_Id"));
        collectionSearchResponse.setCustomerName(resultSet.getString("Customer_Name"));
        collectionSearchResponse.setStatus(resultSet.getBoolean("Status"));
        collectionSearchResponse.setOrderDate(resultSet.getTimestamp("Date"));
        collectionSearchResponse.setTotalAmount(resultSet.getInt("TotalAmount"));
        collectionSearchResponse.setBillNo(resultSet.getString("Bill_No"));
        collectionSearchResponse.setBalanceDue(resultSet.getInt("Balance_Due"));
        collectionSearchResponse.setAssignedStatus(resultSet.getBoolean("Assigned_Status"));
        collectionSearchResponse.setAssigneeName(resultSet.getString("Assignee_Name"));
        collectionSearchResponse.setUpdatedDate(resultSet.getTimestamp("Updated_Date"));

        return collectionSearchResponse;
    }
}
