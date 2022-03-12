package com.kuber.service;

import com.kuber.Authentication.JwtUtils;
import com.kuber.model.LoginRequest;
import com.kuber.model.LoginResponse;
import com.kuber.model.SignupRequest;
import com.kuber.model.User;
import com.kuber.service.mapper.UserDetailsMapper;
import com.kuber.service.mapper.UserResponseRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    private static Logger LOGGER = LoggerFactory.getLogger(OrdersService.class);

    private static final String GET_USER_BY_USER_NAME = "SELECT count(*) from User where User_Name=:userName";
    private static final String GET_USER_BY_EMAIL = "SELECT count(*) from User where Email=:email";
    private static final String INSERT_USER = "INSERT INTO User(User_Name, Email, Password) VALUES ( :userName, :email, :password)";
    private static final String RESET_PASSWORD = "Update User set Password=:password where User_Name=:userName";


    public String signup(SignupRequest signupRequest, String token) throws DuplicateKeyException, SQLException, AccessDeniedException {
        SqlParameterSource namedParameters = new BeanPropertySqlParameterSource(signupRequest);
        try{
            Collection<? extends GrantedAuthority> roles = jwtUtils.getRolesFromJwtToken(token);
             boolean isSuperAdmin = false;
            for (GrantedAuthority grantedAuthority : roles) {
                if ((grantedAuthority.getAuthority()).equals("Super_Admin")) {
                    isSuperAdmin = true;
                }
            }
            if(!isSuperAdmin){
                throw new AccessDeniedException("You are not authorized to Enroll a User");
            }
            int userCount = this.namedParameterJdbcTemplate.queryForObject(GET_USER_BY_USER_NAME, namedParameters, Integer.class);
            if (userCount != 0) {
                throw new DuplicateKeyException("User already exists!");
            }

            int userEmailCount = this.namedParameterJdbcTemplate.queryForObject(GET_USER_BY_EMAIL, namedParameters, Integer.class);
            if (userEmailCount != 0) {
                throw new DuplicateKeyException("Email already exists!");
            }

            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("userName", signupRequest.getUserName());
            parameters.addValue("email", signupRequest.getEmail());
            parameters.addValue("password", encoder.encode(signupRequest.getPassword()));

            int rowsAffected = this.namedParameterJdbcTemplate.update(INSERT_USER, parameters);
            if (rowsAffected != 1) {
                throw new SQLException("Failed to add User");
            }
            return "Registered user successfully";
        }catch (DuplicateKeyException e) {
            throw new DuplicateKeyException(e.getMessage());
        }
        catch (AccessDeniedException e) {
            throw new AccessDeniedException("You are not authorized to Enroll a User");
        }
        catch (Exception e) {
            throw new SQLException("SQL Error ", e);
        }
    }

    /* Login Flow
        Configure filter in webSecurityconfig
        authenticationManager -> authenticate function which dispatches UsernamePasswordAuthenticationToken,
         UsernamePasswordAuthenticationToken gets userName from login request and sets to principal,
        authenticationManager has DaoAuthentication Provider which calls userInfo from database (userService - loadUserByUserName)
        abstractUserDetailAuthenticationProvider checks account locked, expired, disabled (userDetailMapper) else throw error
        DaoAuthentication Provider checks for credentials from request :
           if not present throws error Bad credentials
           else if not matches throws error Bad credentials
           it matches for password from db and user provided password
         AbstractDetailsAuthenticationProvider - sets authentication details like principal , credentials ->
         JwtUtils has generateJwtToken function - which generated token HS512 algorithm and then return response
         */
    public LoginResponse login(LoginRequest loginRequest) throws InternalAuthenticationServiceException, SQLException {

        try{
            LoginResponse loginResponse = new LoginResponse();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsMapper userDetails = (UserDetailsMapper) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            loginResponse.setToken(jwt);
            loginResponse.setRoles(roles);
            loginResponse.setUserId(userDetails.getUserId());
            loginResponse.setEmail(userDetails.getEmail());
            loginResponse.setUserName(userDetails.getUsername());

            return  loginResponse;
        }catch(InternalAuthenticationServiceException e){
            throw new InternalAuthenticationServiceException("User do not exist!");
        } catch(BadCredentialsException e){
            throw new BadCredentialsException("UserName or Password is incorrect!");
        }
        catch (Exception e) {
            throw new SQLException("SQL Error ", e);
        }

    }

    public void logout() throws Exception {
        try{
            SecurityContextHolder.getContext().setAuthentication(null);
        }catch(Exception e){
            throw new Exception(e);
        }
    }

    public String resetPassword(LoginRequest resetPasswordRequest, String token) throws NotFoundException, SQLException, AccessDeniedException {
        try{

            MapSqlParameterSource parameters = new MapSqlParameterSource();
            parameters.addValue("userName", resetPasswordRequest.getUsername());
            parameters.addValue("password", encoder.encode(resetPasswordRequest.getPassword()));

            String userName = jwtUtils.getUserNameFromJwtToken(token);
            if(!userName.equals(resetPasswordRequest.getUsername())){
                throw new AccessDeniedException("Trying to reset password for another user!");
            }
            int userCount = this.namedParameterJdbcTemplate.queryForObject(GET_USER_BY_USER_NAME, parameters, Integer.class);
            if (userCount == 0) {
                throw new NotFoundException("User Not Found!");
            }

            int rowsAffected = this.namedParameterJdbcTemplate.update(RESET_PASSWORD, parameters);
            if (rowsAffected != 1) {
                throw new SQLException("Failed to Reset Password");
            }
            return "Password Reset successfully";
        }catch (NotFoundException e) {
            throw new DuplicateKeyException("User Not Found!");
        }catch (AccessDeniedException e) {
            throw new AccessDeniedException("Trying to reset password for another user!");
        }
        catch (Exception e) {
            throw new SQLException("SQL Error ", e);
        }
    }
}
