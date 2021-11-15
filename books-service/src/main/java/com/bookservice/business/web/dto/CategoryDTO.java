package com.bookservice.business.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDTO {
    @Schema(description = "Id of the category",
            name = "id",
            example = "81")
    private Long id;

    @Schema(description = "Name of the category",
            name = "name",
            example = "Romance")
    private String name;
}
