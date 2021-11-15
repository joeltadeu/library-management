package com.bookservice.business.web.controller.utils;

public class BookControllerUtils {
    public static String getCreateBookRequestJson(String title, String isbn, Long authorId, Long categoryId) {
        return """
                {
                    "title":"%s",
                    "isbn":"%s",
                    "authorId":%s,
                    "categories": [%s]
                }
            """.formatted(title, isbn, authorId, categoryId);
    }

    public static String getCreateBookRequestWithoutTitleJson() {
        return """
                {
                    "title":null,
                    "isbn":"978-14-09091-15-8",
                    "authorId":13,
                    "categories": [14, 15]
                }
            """;
    }

    public static String getCreateBookRequestWithoutIsbnJson() {
        return """
                {
                    "title":"Da Vinci Code",
                    "isbn":null,
                    "authorId":13,
                    "categories": [14, 15]
                }
            """;
    }

    public static String getCreateBookRequestWithoutAuthorJson() {
        return """
                {
                    "title":"Da Vinci Code",
                    "isbn":"978-14-09091-15-8",
                    "authorId":null,
                    "categories": [14, 15]
                }
            """;
    }
    public static String getUpdateBookRequestJson() {
        return """
                {
                    "title":"Da Vinci Code",
                    "isbn":"978-14-09091-15-8",
                    "authorId":1,
                    "categories": [14, 15]
                }
            """;
    }
}
