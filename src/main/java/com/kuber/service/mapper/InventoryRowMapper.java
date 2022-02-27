package com.kuber.service.mapper;

import com.kuber.model.InventoryResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryRowMapper implements RowMapper<InventoryResponse> {

    @Override
    public InventoryResponse mapRow(ResultSet resultSet, int i) throws SQLException {
        InventoryResponse inventory = new InventoryResponse();

        inventory.setInventoryId(resultSet.getInt("Inventory_Id"));
        inventory.setProductId(resultSet.getInt("Product_Id"));
        inventory.setQuantity(resultSet.getInt("Quantity"));
        inventory.setProductionDate(resultSet.getTimestamp("Date"));
        inventory.setProductType(resultSet.getString("Product_Type"));

        return inventory;
    }
}
