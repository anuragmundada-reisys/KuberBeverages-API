package com.kuber.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
public class ExpenseResponse {
    private int expenseId;

    private String expenseType;

    private int amount;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expenseDate;

}
