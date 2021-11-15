package com.bookservice.business.web.dto.criteria;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Data
public class BookSearchCriteria {
    private String title;
    private String isbn;
    private String author;
    private Set<String> categories;
}
