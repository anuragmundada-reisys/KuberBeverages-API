package com.kuber.service.mapper;

import com.kuber.model.RawMaterialPurchase;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RawMaterialsRowMapper implements RowMapper<RawMaterialPurchase> {

    @Override
    public RawMaterialPurchase mapRow(ResultSet resultSet, int i) throws SQLException {
        RawMaterialPurchase rawMaterialPurchase = new RawMaterialPurchase();

        rawMaterialPurchase.setPurchaseId(resultSet.getInt("Raw_Material_Purchase_Id"));
        rawMaterialPurchase.setRawMaterialId(resultSet.getInt("Raw_Material_Id"));
        rawMaterialPurchase.setProductId(resultSet.getInt("Product_Id"));
        rawMaterialPurchase.setQuantity(resultSet.getInt("Quantity"));
        rawMaterialPurchase.setStatus(resultSet.getBoolean("Status"));
        rawMaterialPurchase.setPurchaseDate(resultSet.getTimestamp("Date"));
        rawMaterialPurchase.setRawMaterialName(resultSet.getString("Raw_Material_Name"));
        rawMaterialPurchase.setProductType(resultSet.getString("Product_Type"));

        return rawMaterialPurchase;
    }
}
