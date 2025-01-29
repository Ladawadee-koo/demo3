package com.example.demo3.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRequest {

    @JsonProperty("account_id")
    private String accountId;

    @JsonProperty("withdraw_amount")
    private BigDecimal withdrawAmount;
}
