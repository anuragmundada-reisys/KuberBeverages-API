package com.kuber.controller;

import com.kuber.model.Dictionary;
import com.kuber.service.DictionaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/kuberbeverages/dictionary/v1")
@Tag(name = "Dictionary Controller", description = "Get Master data")
public class DictionaryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DictionaryController.class);

    @Autowired
    private DictionaryService dictionaryService;

    @Operation(summary = "Get All Dictionaries")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Dictionaries", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getDictionaries() throws SQLException {
        Map<String, List<Dictionary>> dictionaries = dictionaryService.getAllDictionaries();
        return new ResponseEntity<>(dictionaries, HttpStatus.OK);
    }

    @Operation(summary = "Get All Customer Names")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Customer Names", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(value="/customers" ,method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getCustomerNames() throws SQLException {
        List<String> customerNames = dictionaryService.getCustomerNames();
        return new ResponseEntity<>(customerNames, HttpStatus.OK);
    }

    @Operation(summary = "Get All Payment Receiver Names")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Payment Receiver Names", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(value="/receivers" ,method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getReceivers() throws SQLException {
        List<String> customerNames = dictionaryService.getReceivers();
        return new ResponseEntity<>(customerNames, HttpStatus.OK);
    }

    @Operation(summary = "Get All Bill Numbers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Customer Names", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(value="/billno" ,method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getAllBillNo() throws SQLException {
        List<String> customerNames = dictionaryService.getBillNumbers();
        return new ResponseEntity<>(customerNames, HttpStatus.OK);
    }
}
