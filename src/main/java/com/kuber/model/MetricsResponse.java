package com.kuber.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MetricsResponse {

    private int productId;

    private String title;

    private int pendingOrders;

    private int availableStock;

    private List<RawMaterialDictionary> availableRawMaterial;

}
