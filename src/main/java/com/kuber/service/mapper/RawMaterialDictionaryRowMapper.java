package com.kuber.service.mapper;

import com.kuber.model.RawMaterialDictionary;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RawMaterialDictionaryRowMapper implements RowMapper<RawMaterialDictionary> {
    @Override
    public RawMaterialDictionary mapRow(ResultSet resultSet, int i) throws SQLException {
        RawMaterialDictionary rawMaterialDictionary = new RawMaterialDictionary();

        rawMaterialDictionary.setName(resultSet.getString("label"));
        rawMaterialDictionary.setCount(resultSet.getInt("count"));

        return rawMaterialDictionary;
    }
}
