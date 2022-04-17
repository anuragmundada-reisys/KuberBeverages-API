package com.kuber.controller;

import com.kuber.model.AvailableStockMetricsResponse;
import com.kuber.model.Dictionary;
import com.kuber.model.MetricsResponse;
import com.kuber.model.TotalBalanceDueMetricsResponse;
import com.kuber.service.MetricsService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/kuberbeverages/metrics/v1")
@Tag(name = "Metrics Controller", description = "Get metrics for available stock and pie chart data")
public class MetricsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetricsController.class);

    @Autowired
    private MetricsService metricsService;

    @Operation(summary = "Get All Metrics")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Metrics", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping( method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getMetrics(@RequestParam("receivedDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") Date receivedDate) throws SQLException {
        Map<String, List<Dictionary>> metricsList = metricsService.getMetrics(receivedDate);
        return new ResponseEntity<>(metricsList, HttpStatus.OK);
    }

    @Operation(summary = "Get available stock")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Metrics", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping( value = "/availablestock" , method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getAvailableStock() throws SQLException {
        List<AvailableStockMetricsResponse> metricsList = metricsService.getAvailableStock();
        return new ResponseEntity<>(metricsList, HttpStatus.OK);
    }

    @Operation(summary = "Get Total BalanceDue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Metrics", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping( value = "/totalbalancedue" , method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getBalanceDue() throws SQLException {
        List<TotalBalanceDueMetricsResponse> metricsList = metricsService.getBalanceDue();
        return new ResponseEntity<>(metricsList, HttpStatus.OK);
    }

}
