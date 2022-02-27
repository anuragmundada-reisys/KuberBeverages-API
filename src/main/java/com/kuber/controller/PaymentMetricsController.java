package com.kuber.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuber.model.MetricsResponse;
import com.kuber.model.PaymentMetricsResponse;
import com.kuber.service.MetricsService;
import com.kuber.service.PaymentHistoryService;
import com.kuber.service.PaymentMetricsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/kuberbeverages/paymentmetrics/v1")
@Tag(name = "Payment Metrics Controller", description = "Get total amount received through Cash/Gpay as per data")
public class PaymentMetricsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(PaymentMetricsController.class);

    @Autowired
    private PaymentMetricsService paymentMetricsService;

    @Operation(summary = "Get total amount Metrics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Metrics", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping( method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getPaymentMetrics(@RequestParam("receivedDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date receivedDate) throws SQLException {
        List<PaymentMetricsResponse> metricsList = paymentMetricsService.getPaymentMetrics(receivedDate);
        return new ResponseEntity<>(metricsList, HttpStatus.OK);
    }

}
