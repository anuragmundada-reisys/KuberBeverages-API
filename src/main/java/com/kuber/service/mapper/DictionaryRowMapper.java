package com.kuber.service.mapper;

import com.kuber.model.Dictionary;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DictionaryRowMapper implements RowMapper<Dictionary> {
    @Override
    public Dictionary mapRow(ResultSet resultSet, int i) throws SQLException {
        Dictionary dictionary = new Dictionary();

        dictionary.setKey(resultSet.getInt("key"));
        dictionary.setValue(resultSet.getString("value"));

        return dictionary;
    }
}
