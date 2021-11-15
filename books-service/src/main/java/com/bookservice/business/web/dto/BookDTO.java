package com.bookservice.business.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDTO {
    @Schema(description = "Id of the book",
            name = "id",
            example = "12")
    private Long id;

    @Schema(description = "Title of the book",
            name = "title",
            example = "Little Prince")
    private String title;

    @Schema(description = "ISBN of the book",
            name = "isbn",
            example = "Little Prince")
    private String isbn;

    @Schema(description = "Author of the book",
            name = "author")
    private AuthorDTO author;

    @Schema(description = "Categories of the book",
            name = "categories")
    private Set<CategoryDTO> categories;
}
