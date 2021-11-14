package com.orderservice.business.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamDTO {
    @Schema(description = "Param's key", name = "key", example = "borrow-limit")
    private String key;

    @Schema(description = "Param's value", name = "value", example = "3")
    private Integer value;
}
