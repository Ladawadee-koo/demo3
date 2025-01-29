package com.example.demo3.controller.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GeneralResponse<T> {
    @JsonProperty("code")
    private String code;

    @JsonProperty("data")
    private T data;
}
