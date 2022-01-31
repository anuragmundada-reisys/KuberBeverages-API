package com.kuber.controller;

import com.kuber.model.Inventory;
import com.kuber.model.InventoryRequest;
import com.kuber.model.Orders;
import com.kuber.model.OrdersRequest;
import com.kuber.service.InventoryService;
import com.kuber.service.OrdersService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/kuberbeverages/inventory/v1")
@Tag(name = "Inventory Controller", description = "Add Inventory")
public class InventoryController {
    private static final Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    private InventoryService inventoryService;

    @Operation(summary = "Get Inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get All Inventory data",
                    content = { @Content(mediaType = "application/json", schema = @Schema(implementation = Orders.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<Inventory>> getOrders() throws SQLException {
        List<Inventory> inventoryData = inventoryService.getInventory();
        return new ResponseEntity<>(inventoryData, HttpStatus.OK);
    }

    @Operation(summary = "Add Inventory")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Inventory created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(method = RequestMethod.POST, produces = "application/text")
    public ResponseEntity<Object> inventory(@RequestBody InventoryRequest inventoryRequest) {
        try {
            List<String> errorList = Utility.validateInventoryRequest(inventoryRequest);
            if (!errorList.isEmpty()) {
                return new ResponseEntity<>(errorList.toString(), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(inventoryService.addInventory(inventoryRequest) + "", HttpStatus.CREATED);
            }
        } catch (Exception e) {
            LOGGER.error("Error adding Inventory: ", e);
            return  new ResponseEntity<>("Internal error adding data in Inventory.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
