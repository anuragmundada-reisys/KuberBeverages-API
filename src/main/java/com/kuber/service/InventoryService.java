package com.kuber.service;

import com.kuber.model.Inventory;
import com.kuber.model.InventoryRequest;
import com.kuber.model.Orders;
import com.kuber.model.OrdersRequest;
import com.kuber.service.mapper.InventoryRowMapper;
import com.kuber.service.mapper.OrderRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static Logger LOGGER = LoggerFactory.getLogger(OrdersService.class);

    private static final String INSERT_INVENTORY = "INSERT INTO Inventory(Product_Id, Quantity, Date) VALUES (:productId, :quantity, :productionDate)";
    private static final String GET_INVENTORY = "SELECT inventory.*, p.Product_Type FROM Inventory inventory LEFT JOIN  Product p ON inventory.Product_Id = p.Product_Id order by Inventory_Id desc";

    @Transactional
    public String addInventory(InventoryRequest inventoryRequest) throws SQLException {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(inventoryRequest);
        int rowsAffected = this.namedParameterJdbcTemplate.update(INSERT_INVENTORY, namedParameters);

        if (rowsAffected != 1) {
            throw new SQLException("Failed to insert Inventory: " + inventoryRequest.toString());
        }
        return "Inventory successfully added";
    }

    public List<Inventory> getInventory() throws SQLException {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            return this.namedParameterJdbcTemplate.query(GET_INVENTORY, parameters, new InventoryRowMapper());
        } catch (Exception e) {
            throw new SQLException("SQL Error ", e);
        }
    }
}
