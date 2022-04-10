package com.kuber.controller;

import com.kuber.model.LoginRequest;
import com.kuber.model.LoginResponse;
import com.kuber.model.SignupRequest;
import com.kuber.service.AuthService;
import com.kuber.utility.Utility;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.nio.file.AccessDeniedException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/kuberbeverages/auth/v1")
@Tag(name = "Auth Controller", description = "Authentication")
public class AuthController {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @Operation(summary = "Register a User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Register a User",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SignupRequest.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})

    @RequestMapping( value = "/signup",method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<String> signup(@RequestBody SignupRequest signupRequest, @RequestHeader (name="Authorization") String token) throws SQLException {
        try {
            List<String> errorList = Utility.validateSignUpRequest(signupRequest);
            if (!errorList.isEmpty()) {
                return new ResponseEntity<>(errorList.get(0), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(authService.signup(signupRequest, token) + "", HttpStatus.CREATED);
            }
        } catch (DuplicateKeyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (AccessDeniedException e) {
            return new ResponseEntity<>("You are not authorized to Enroll a User", HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e) {
            LOGGER.error("Error Registering User: ", e);
            return  new ResponseEntity<>("Internal error Registering User. " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Login a User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Login",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SignupRequest.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping( value = "/login",method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) throws SQLException {
        try {
            List<String> errorList = Utility.validateLoginRequest(loginRequest);
            if (!errorList.isEmpty()) {
                return new ResponseEntity<>(errorList.get(0), HttpStatus.BAD_REQUEST);
            } else {
                LoginResponse loginResponse = authService.login(loginRequest);
                return new ResponseEntity<>(loginResponse, HttpStatus.OK);
            }
        }
        catch(InternalAuthenticationServiceException e){
            LOGGER.error("User do not exist!");
            return new ResponseEntity<>("User does not exist.", HttpStatus.UNAUTHORIZED);
        }catch(BadCredentialsException e){
            LOGGER.error("Bad Credentials");
            return new ResponseEntity<>("User Name or Password is incorrect.", HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e) {
            LOGGER.error("Error authenticating User: ", e);
            return  new ResponseEntity<>("Internal error authenticating User. " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Logout a User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User Login",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SignupRequest.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping( value = "/logout",method = RequestMethod.GET, produces = "application/json")
    public void logout() throws SQLException {
        try {
              authService.logout();
        }  catch (Exception e) {
            LOGGER.error("Error Logging out User: ", e);
           // return  new ResponseEntity<>("Internal error authenticating User. " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Reset Password")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reset Password",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = SignupRequest.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping( value = "/resetpassword",method = RequestMethod.PATCH, produces = "application/json")
    public ResponseEntity<Object> resetPassword(@RequestBody LoginRequest resetPasswordRequest, @RequestHeader (name="Authorization") String token) throws SQLException {
        try {
            List<String> errorList = Utility.validateLoginRequest(resetPasswordRequest);
            if (!errorList.isEmpty()) {
                return new ResponseEntity<>(errorList.get(0), HttpStatus.BAD_REQUEST);
            } else {
                String response = authService.resetPassword(resetPasswordRequest, token);
                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        catch(NotFoundException e){
            LOGGER.error("User not found");
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }catch (AccessDeniedException e) {
            return new ResponseEntity<>("Unauthorized: Trying to reset password for another user!", HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e) {
            LOGGER.error("Error resetting password: ", e);
            return  new ResponseEntity<>("Internal error resetting password. " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
