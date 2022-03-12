package com.kuber.service;

import com.kuber.model.Role;
import com.kuber.model.User;
import com.kuber.service.mapper.RoleMapper;
import com.kuber.service.mapper.UserDetailsMapper;
import com.kuber.service.mapper.UserResponseRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private static final String GET_USER_BY_USER_NAME = "SELECT * FROM User where User_Name=:userName";
    private static final String GET_ROLES_BY_USER_ID = "SELECT ur.Role_Id, r.Role_Name FROM User_Role ur  left join Role r on ur.Role_Id = r.Role_Id where User_Id=:userId";

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userName", userName);
        try{
            User user = this.namedParameterJdbcTemplate.queryForObject(GET_USER_BY_USER_NAME, parameters, new UserResponseRowMapper());
            parameters.addValue("userId", user.getUserId());
            List<Role> roles = this.namedParameterJdbcTemplate.query(GET_ROLES_BY_USER_ID, parameters, new RoleMapper());
            user.setRoles(roles);
            return UserDetailsMapper.build(user);
        }catch(UsernameNotFoundException e){
            throw new UsernameNotFoundException("User Not Found with username: " + userName);
        }
    }
}
