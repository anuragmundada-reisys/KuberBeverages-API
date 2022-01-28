package com.kuber.controller;

import com.kuber.model.RawMaterialPurchase;
import com.kuber.model.RawMaterialPurchaseRequest;
import com.kuber.service.RawMaterialsService;
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
@RequestMapping("/kuberbeverages/rawmaterials/v1")
@Tag(name = "Acquisition Controller", description = "Get RawMaterial Details")
public class RawMaterialsController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RawMaterialsController.class);

    @Autowired
    private RawMaterialsService rawMaterialsService;

    @Operation(summary = "Get All RawMaterials")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get RawMaterial Details",
                content = { @Content(mediaType = "application/json", schema = @Schema(implementation = RawMaterialPurchase.class))}),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<RawMaterialPurchase>> getRawMaterialsPurchaseList() throws SQLException {
        List<RawMaterialPurchase> rawMaterials = rawMaterialsService.getAllRawMaterialsPurchases();
        return new ResponseEntity<>(rawMaterials, HttpStatus.OK);
    }

    @Operation(summary = "Purchase RawMaterial")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "RawMaterial created", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
    @RequestMapping(method = RequestMethod.POST, produces = "application/text")
    public ResponseEntity<Object> purchaseRawMaterials(@RequestBody RawMaterialPurchaseRequest rawMaterialPurchaseRequest) {
        try {
            List<String> errorList = Utility.validateRawMaterialsPurchaseRequest(rawMaterialPurchaseRequest);
            if (!errorList.isEmpty()) {
                return new ResponseEntity<>(errorList.toString(), HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>(rawMaterialsService.addRawMaterialsPurchase(rawMaterialPurchaseRequest) + "", HttpStatus.CREATED);
            }
        } catch (Exception e) {
            LOGGER.error("Error adding RawMaterial purchase: ", e);
            return  new ResponseEntity<>("Internal error adding RawMaterial purchase.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @Operation(summary = "Delete an RawMaterial")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "RawMaterial Deleted", content = @Content),
//            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
//            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
//            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
//            @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)})
//    @RequestMapping(value = "/award/{id}", method = RequestMethod.DELETE)
//    public ResponseEntity<Object> deleteAward(@PathVariable String id) {
//        RawMaterial award = acquisitionService.getAwardDetailsById(id);
//        if (award == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//        try {
//            acquisitionService.deleteAwardById(id);
//            return new ResponseEntity<>("Successfully Deleted RawMaterial " + id, HttpStatus.OK);
//        } catch (Exception e) {
//            LOGGER.error("Error deleting RawMaterial " + id, e);
//            return new ResponseEntity<>("Internal error deleting RawMaterial.", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

}
