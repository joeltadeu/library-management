package com.orderservice.business.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OperationDTO {
    @Schema(description = "Id of the book",
            name = "bookId",
            example = "52")
    @NotNull(message = "Book was not informed")
    private Long bookId;
    @Schema(description = "Id of the user",
            name = "userId",
            example = "36")
    @NotNull(message = "User was not informed")
    private Long userId;
}
