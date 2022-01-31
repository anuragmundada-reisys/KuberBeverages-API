package com.kuber.service;

import com.kuber.model.Dictionary;
import com.kuber.service.mapper.DictionaryRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DictionaryService {
    private static Logger LOGGER = LoggerFactory.getLogger(RawMaterialsService.class);

    private static final String GET_RAW_MATERIALS_DICTIONARY = "SELECT Raw_Material_Id as 'key', Raw_Material_Name as 'value' from Raw_Material";
    private static final String GET_PRODUCT_TYPE_DICTIONARY = "SELECT Product_Id as 'key', Product_Type as 'value' from Product";

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Map<String, List<Dictionary>> getAllDictionaries() throws SQLException {
        Map<String, List<Dictionary>> dictionaries = new HashMap<>();
        try {
            List<Dictionary> rawMaterialDictionary = this.namedParameterJdbcTemplate.query(GET_RAW_MATERIALS_DICTIONARY, new MapSqlParameterSource(), new DictionaryRowMapper());
            List<Dictionary> productTypeDictionary = this.namedParameterJdbcTemplate.query(GET_PRODUCT_TYPE_DICTIONARY, new MapSqlParameterSource(), new DictionaryRowMapper());

            dictionaries.put("rawMaterialType", rawMaterialDictionary);
            dictionaries.put("productType", productTypeDictionary);

            return dictionaries;
        } catch (Exception e) {
            throw new SQLException("SQL Error ", e);
        }
    }

}
