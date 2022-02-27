package com.kuber.service;

import com.kuber.model.*;
import com.kuber.service.mapper.InventoryRowMapper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Service
public class InventoryService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static Logger LOGGER = LoggerFactory.getLogger(OrdersService.class);

    private static final String INSERT_INVENTORY = "INSERT INTO Inventory(Product_Id, Quantity, Date) VALUES (:productId, :quantity, :productionDate)";
    private static final String GET_INVENTORY = "SELECT inventory.*, p.Product_Type FROM Inventory inventory LEFT JOIN  Product p ON inventory.Product_Id = p.Product_Id order by Inventory_Id desc";
   private static final String SEARCH_INVENTORY = "SELECT inventory.*, p.Product_Type FROM Inventory inventory LEFT JOIN  Product p ON inventory.Product_Id = p.Product_Id";
    @Transactional
    public String addInventory(InventoryRequest inventoryRequest) throws SQLException {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("productionDate", inventoryRequest.getProductReceivedDate());
        for(Inventory inventory: inventoryRequest.getProducts()){
            parameters.addValue("productId", inventory.getProductId());
            parameters.addValue("quantity", inventory.getQuantity());
            int rowsAffected = this.namedParameterJdbcTemplate.update(INSERT_INVENTORY, parameters);

            if (rowsAffected != 1) {
                throw new SQLException("Failed to insert products in Inventory: " + inventoryRequest.toString());
            }
        }

        return "Products successfully added in Inventory";
    }

    public List<InventoryResponse> getInventory() throws SQLException {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            return this.namedParameterJdbcTemplate.query(GET_INVENTORY, parameters, new InventoryRowMapper());
        } catch (Exception e) {
            throw new SQLException("SQL Error ", e);
        }
    }

    public List<InventoryResponse> searchInventory(Map<String, String> params) throws SQLException {
        StringBuilder queryString = new StringBuilder();
        Map<String, String> columnMap = new HashMap<>();
        columnMap.put("productType", "Product_Id");
        columnMap.put("productionDate", "Date");

        queryString.append(SEARCH_INVENTORY);
        queryString.append(" WHERE 1=1 ");
        for (String key : params.keySet()) {
            String value = params.get(key);
            if (isNotBlank(value)) {
                if(key.equalsIgnoreCase("productType")){
                    queryString.append(" AND inventory." + columnMap.get(key) + "=:" + key);
                }else{
                    queryString.append(" AND " + columnMap.get(key) + "=:" + key);
                }
            }
        }
        queryString.append(" order by Inventory_Id desc");
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource(params);
            return this.namedParameterJdbcTemplate.query(queryString.toString(), parameters, new InventoryRowMapper());
        } catch (Exception e) {
            throw new SQLException("SQL Error ", e);
        }
    }
}
