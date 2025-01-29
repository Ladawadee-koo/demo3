package com.example.demo3.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransferRequest {
    @JsonProperty("from_account_id")
    private String fromAccountId;

    @JsonProperty("to_account_id")
    private String toAccountId;

    @JsonProperty("transfer_amount")
    private BigDecimal transferAmount;

}
