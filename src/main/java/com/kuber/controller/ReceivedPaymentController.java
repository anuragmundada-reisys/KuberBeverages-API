package com.kuber.controller;

import com.kuber.model.Orders;
import com.kuber.model.ReceivedPayment;
import com.kuber.service.ReceivedPaymentService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/kuberbeverages/receivedpayment/v1")
@Tag(name = "Order Controller", description = "Add Order")
public class ReceivedPaymentController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private ReceivedPaymentService receivedPaymentService;

    @Operation(summary = "Get Received Payment Details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Received Payment Details",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Orders.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<ReceivedPayment>> getReceivedPayment(@RequestParam(required = false) Map<String, String> params) throws SQLException {
        List<ReceivedPayment> receivedPayments = receivedPaymentService.getReceivedPayment(params);
        return new ResponseEntity<>(receivedPayments, HttpStatus.OK);
    }
}
