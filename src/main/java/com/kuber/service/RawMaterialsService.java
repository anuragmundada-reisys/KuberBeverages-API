package com.kuber.service;

import com.kuber.model.RawMaterialPurchase;
import com.kuber.model.RawMaterialPurchaseRequest;
import com.kuber.service.mapper.RawMaterialsRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RawMaterialsService {

    private static Logger LOGGER = LoggerFactory.getLogger(RawMaterialsService.class);

    private static final String GET_RAW_MATERIALS_PURCHASE_LIST = "SELECT rmp.*, rm.Raw_Material_Name, p.Product_Type FROM Raw_Material_Purchase rmp LEFT JOIN Raw_Material rm ON rmp.Raw_Material_Id = rm.Raw_Material_Id LEFT JOIN Product p ON rmp.Product_Id = p.Product_Id order by Raw_Material_Purchase_Id desc";
    private static final String INSERT_RAW_MATERIALS_PURCHASE = "INSERT INTO Raw_Material_Purchase(Raw_Material_Id, Product_Id, Quantity, Status, Date) VALUES (:rawMaterialId, :productId, :quantity, :status, :purchaseDate)";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public List<RawMaterialPurchase> getAllRawMaterialsPurchases() throws SQLException {
        try {
            MapSqlParameterSource parameters = new MapSqlParameterSource();
            return this.namedParameterJdbcTemplate.query(GET_RAW_MATERIALS_PURCHASE_LIST, parameters, new RawMaterialsRowMapper());
        } catch (Exception e) {
            throw new SQLException("SQL Error ", e);
        }
    }

    @Transactional
    public String addRawMaterialsPurchase(RawMaterialPurchaseRequest rawMaterialPurchaseRequest) throws SQLException {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(rawMaterialPurchaseRequest);
        int rowsAffected = this.namedParameterJdbcTemplate.update(INSERT_RAW_MATERIALS_PURCHASE, namedParameters);

        if (rowsAffected != 1) {
            throw new SQLException("Failed to insert RawMaterial purchase: " + rawMaterialPurchaseRequest.toString());
        }
        return "Raw Material purchase successfully added";
    }

}
