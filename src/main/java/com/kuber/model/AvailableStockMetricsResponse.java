package com.kuber.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AvailableStockMetricsResponse {
    private String title;

    private int availableCases;

    private int availableBottles;

}
