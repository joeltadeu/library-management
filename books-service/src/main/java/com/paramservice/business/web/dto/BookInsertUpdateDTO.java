package com.paramservice.business.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookInsertUpdateDTO {

    @Schema(description = "Title of the book",
            name = "title",
            required = true,
            example = "Little Prince")
    @NotBlank(message = "Title was not informed")
    private String title;

    @Schema(description = "ISBN of the book",
            name = "isbn",
            required = true,
            example = "978-07-9367786-341-10")
    @NotBlank(message = "ISBN was not informed")
    private String isbn;

    @Schema(description = "Author's Id",
            name = "authorId",
            required = true,
            example = "43")
    @NotNull(message = "Author was not informed")
    private Long authorId;

    @Schema(description = "Categories of the book",
            name = "categories",
            required = true,
            example = "[43, 57, 5, 7]")
    @NotEmpty(message="Book needs to have one or more categories")
    private Set<Long> categories;
}
