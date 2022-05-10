package com.kuber.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseRequest {
    private int expenseId;

    List<Expense> expenses;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expenseDate;
}
