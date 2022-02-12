package com.kuber.service.mapper;

import com.kuber.model.Inventory;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InventoryRowMapper implements RowMapper<Inventory> {

    @Override
    public Inventory mapRow(ResultSet resultSet, int i) throws SQLException {
        Inventory inventory = new Inventory();

        inventory.setInventoryId(resultSet.getInt("Inventory_Id"));
        inventory.setProductId(resultSet.getInt("Product_Id"));
        inventory.setQuantity(resultSet.getInt("Quantity"));
        inventory.setProductionDate(resultSet.getTimestamp("Date"));
        inventory.setProductType(resultSet.getString("Product_Type"));

        return inventory;
    }
}
