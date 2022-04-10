package com.kuber.controller;

import com.kuber.model.*;
import com.kuber.service.OrdersService;
import com.kuber.service.PaymentHistoryService;
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

@RestController
@RequestMapping("/kuberbeverages/paymenthistory/v1")
@Tag(name = "Payment History Controller", description = "Add Payment")
public class PaymentHistoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private PaymentHistoryService paymentHistoryService;
    @Operation(summary = "Get Payment History of Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Payment History of Order",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = PaymentHistory.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(value = "/{orderId}", method = RequestMethod.GET)
    public ResponseEntity<List<PaymentHistory>> getPaymentHistory(@PathVariable int orderId) throws SQLException {
        List<PaymentHistory> paymentHistory = paymentHistoryService.getPaymentHistoryByOrderId(orderId);
        return new ResponseEntity<>(paymentHistory, HttpStatus.OK);
    }


    @Operation(summary = "Add Payment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(method = RequestMethod.POST, produces = "application/text")
    public ResponseEntity<Object> addPayment(@RequestBody PaymentHistoryRequest paymentHistoryRequest, @RequestHeader (name="Authorization") String token) {
        try {
                return new ResponseEntity<>(paymentHistoryService.addPaymentHistory(paymentHistoryRequest, token) + "", HttpStatus.CREATED);
        } catch (Exception e) {
            LOGGER.error("Error adding Payment: ", e);
            return  new ResponseEntity<>("Internal error adding Payment.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
