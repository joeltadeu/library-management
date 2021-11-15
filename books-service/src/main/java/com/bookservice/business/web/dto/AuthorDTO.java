package com.bookservice.business.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDTO {
    @Schema(description = "Id of the author",
            name = "id",
            example = "36")
    private Long id;

    @Schema(description = "Name of the author",
            name = "name",
            example = "Paul Connor")
    private String name;
}
