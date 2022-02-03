package com.kuber.controller;

import com.kuber.model.MetricsResponse;
import com.kuber.service.MetricsService;
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
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<Object> getMetrics() throws SQLException {
        List<MetricsResponse> metricsList = metricsService.getMetrics();
        return new ResponseEntity<>(metricsList, HttpStatus.OK);
    }

}
