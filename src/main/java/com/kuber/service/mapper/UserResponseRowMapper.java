package com.kuber.service.mapper;

import com.kuber.model.Orders;
import com.kuber.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserResponseRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();

        user.setUserId(resultSet.getInt("User_Id"));
        user.setUserName(resultSet.getString("User_Name"));
        user.setEmail(resultSet.getString("Email"));
        user.setPassword(resultSet.getString("Password"));
        return user;
    }
}
