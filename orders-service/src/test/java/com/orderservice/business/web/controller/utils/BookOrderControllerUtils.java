package com.orderservice.business.web.controller.utils;

import com.orderservice.business.persistence.entity.BookOrderEntity;

import java.time.LocalDateTime;

public class BookOrderControllerUtils {
    public static String getLoanReturnBookRequestJson(Long bookId, Long userId) {
        return """
                {
                    "bookId":%s,
                    "userId":%s
                }
            """.formatted(bookId, userId);
    }

    public static String getLoanReturnBookRequestWithoutBookIdJson() {
        return """
                 {
                    "bookId":null,
                    "userId":1
                }
            """;
    }

    public static String getLoanReturnBookRequestWithoutUserIdJson() {
        return """
                 {
                    "bookId":1,
                    "userId":null
                }
            """;
    }

    public static BookOrderEntity getBookOrderEntity(Long orderId, Long bookId, Long userId,
                                                               LocalDateTime returnedAt) {
        BookOrderEntity orderEntity = getBookOrderEntity(orderId, bookId, userId);
        orderEntity.setReturnedAt(returnedAt);
        return orderEntity;
    }

    public static BookOrderEntity getBookOrderEntity(Long orderId, Long bookId, Long userId) {
        return BookOrderEntity.builder()
            .id(orderId)
            .bookId(bookId)
            .userId(userId)
            .loanedAt(LocalDateTime.now())
            .returnIn(LocalDateTime.now().plusDays(5))
            .build();
    }
}
