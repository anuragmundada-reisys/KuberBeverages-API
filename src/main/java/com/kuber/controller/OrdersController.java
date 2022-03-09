package com.kuber.controller;

import com.kuber.model.OrderResponse;
import com.kuber.model.Orders;
import com.kuber.model.OrdersRequest;
import com.kuber.service.OrdersService;
import com.kuber.model.AssignHistoryResponse;
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
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/kuberbeverages/orders/v1")
@Tag(name = "Order Controller", description = "Add Order")
public class OrdersController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrdersController.class);

    @Autowired
    private OrdersService orderService;

    @Operation(summary = "Get Orders")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Orders",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Orders.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<OrderResponse>> getOrders(@RequestParam(required = false) Map<String, String> params) throws SQLException {
        List<OrderResponse> orders = orderService.getOrders(params);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @Operation(summary = "Add Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(method = RequestMethod.POST, produces = "application/text")
    public ResponseEntity<Object> order(@RequestBody OrdersRequest orderRequest, @RequestHeader (name="Authorization") String token) {
        try {
            List<String> errorList = Utility.validateOrderRequest(orderRequest);
            if (!errorList.isEmpty()) {
                return new ResponseEntity<>(errorList.get(0), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(orderService.addOrder(orderRequest, token) + "", HttpStatus.CREATED);
            }
        } catch (DuplicateKeyException e) {
            return new ResponseEntity<>("Duplicate Entry Found.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            LOGGER.error("Error adding Order: ", e);
            return  new ResponseEntity<>("Internal error adding Order. " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update Order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order updated", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(method = RequestMethod.PATCH, produces = "application/text")
    public ResponseEntity<Object> updateOrder(@RequestBody OrdersRequest orderRequest, @RequestHeader (name="Authorization") String token) {
        try {
            List<String> errorList = Utility.validateUpdateOrderRequest(orderRequest);
            if (!errorList.isEmpty()) {
                return new ResponseEntity<>(errorList.toString(), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(orderService.updateOrder(orderRequest, token) + "", HttpStatus.CREATED);
            }
        } catch (Exception e) {
            LOGGER.error("Error updating Order: ", e);
            return  new ResponseEntity<>("Internal error updating Order.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Order Assignee History")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Orders",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Orders.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(value = "assigneehistory/{orderId}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<AssignHistoryResponse>> getOrderAssigneeHistory(@PathVariable int orderId) throws SQLException {
        List<AssignHistoryResponse> orders = orderService.getOrderAssigneeHistory(orderId);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
