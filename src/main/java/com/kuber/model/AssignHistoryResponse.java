package com.kuber.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kuber.model.OrderDetails;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class AssignHistoryResponse {

    private int orderId;

    private String billNo;

    private boolean assignedStatus;

    private String assigneeName;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date updatedDate;

}
