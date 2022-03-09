package com.kuber.service.mapper;

import com.kuber.model.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RoleMapper implements RowMapper<Role> {

    @Override
    public Role mapRow(ResultSet resultSet, int i) throws SQLException {
        Role role = new Role();

        role.setRoleId(resultSet.getInt("Role_Id"));
        role.setRoleName(resultSet.getString("Role_Name"));

        return role;
    }
}
