package com.example.monetization.system.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseEntityDto<T> {
    private String message;
    private T data;

    public ResponseEntityDto(String message) {
        this.message = message;
    }
}
