package com.orderservice.business.web.controller.utils;

import com.orderservice.business.persistence.entity.BookOrderEntity;
import com.orderservice.business.web.dto.OperationDTO;
import com.orderservice.business.web.dto.ParamDTO;
import feign.Param;

import java.time.LocalDateTime;

import static com.orderservice.business.web.controller.utils.TestUtils.getRandomLong;
import static com.orderservice.business.web.controller.utils.TestUtils.getRandomString;
public class BookOrderServiceUtils {


    public static OperationDTO getLoanReturnOperation(Long bookId, Long userId) {
        return new OperationDTO(bookId, userId);
    }

    public static BookOrderEntity getLoanOrder(Long bookId, Long userId) {
        return BookOrderEntity.builder()
            .id(getRandomLong())
            .bookId(bookId)
            .userId(userId)
            .loanedAt(LocalDateTime.now())
            .returnIn(LocalDateTime.now().plusDays(5))
            .build();
    }

    public static ParamDTO getBorrowLimitParam(String key) {
        return new ParamDTO(key, 3);
    }

    public static ParamDTO getBorrowDaysParam(String key) {
        return new ParamDTO(key, 5);
    }
}
