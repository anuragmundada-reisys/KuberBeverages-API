package com.kuber.controller;

import com.kuber.model.Expense;
import com.kuber.model.ExpenseRequest;
import com.kuber.model.ExpenseResponse;
import com.kuber.model.Orders;
import com.kuber.service.ExpenseService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/kuberbeverages/expense/v1")
@Tag(name = "Expense Controller", description = "Add Expense")
public class ExpenseController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExpenseController.class);

    @Autowired
    private ExpenseService expenseService;

    @Operation(summary = "Get Expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Expenses",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Orders.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<ExpenseResponse>> getExpense(@RequestParam(required = false) Map<String, String> params) throws SQLException {
        List<ExpenseResponse> expenses = expenseService.getExpense(params);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @Operation(summary = "Add Expense")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventory created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(method = RequestMethod.POST, produces = "application/text")
    public ResponseEntity<Object> expense(@RequestBody ExpenseRequest expenseRequest, @RequestHeader (name="Authorization") String token) {
        try {
            List<String> errorList = Utility.validateExpenseRequest(expenseRequest);
            if (!errorList.isEmpty()) {
                return new ResponseEntity<>(errorList.get(0), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(expenseService.addExpense(expenseRequest, token) + "", HttpStatus.CREATED);
            }
        } catch (Exception e) {
            LOGGER.error("Error adding Expense: ", e);
            return new ResponseEntity<>("Internal error adding data in Expense.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
