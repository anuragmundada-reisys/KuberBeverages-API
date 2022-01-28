package com.kuber.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter @Setter @NoArgsConstructor
public class RawMaterialPurchase {

    private int purchaseId;

    private int rawMaterialId;

    private int productId;

    private int quantity;

    private boolean status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date purchaseDate;

    private String rawMaterialName;

    private String productType;

}
