package com.kuber.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@NoArgsConstructor
public class Orders {

    private int orderId;

    private String customerName;

    private boolean assignedStatus;

    private String assigneeName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orderDate;
}
