package com.orderservice.business.service;

import com.orderservice.business.persistence.entity.BookOrderEntity;
import com.orderservice.business.persistence.repository.BookOrderRepository;
import com.orderservice.business.service.client.ParamServiceClient;
import com.orderservice.business.web.dto.OperationDTO;
import com.orderservice.business.web.dto.ParamDTO;
import com.orderservice.commons.exception.DataNotFoundException;
import com.orderservice.commons.validation.RestPreConditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class BookOrderService {

    @Value("${lmgn.params.limit}")
    private String borrowLimitKey;

    @Value("${lmgn.params.days}")
    private String borrowDaysKey;

    private final BookOrderRepository repository;
    private final ParamServiceClient paramClient;

    public BookOrderService(
        BookOrderRepository repository, ParamServiceClient paramClient) {
        this.repository = repository;
        this.paramClient = paramClient;
    }

    public BookOrderEntity loaned(OperationDTO operation) {

        log.info("Get param borrow-limit from params-service...");
        ParamDTO borrowLimitParam = paramClient.findByKey(borrowLimitKey);
        log.info("borrow-limit: {}", borrowLimitParam);

        log.info("Get param borrow-days from params-service...");
        ParamDTO borrowDaysParam = paramClient.findByKey(borrowDaysKey);
        log.info("borrow-days: {}", borrowDaysParam);

        log.info("Before loan, checking if the book is available to be loaned");
        RestPreConditions.checkState(
            repository.hasBookLoaned(operation.getBookId()),
            "Book is not available to loan");

        log.info("Before loan, checking if user has books outstanding to be returned...");
        RestPreConditions.checkState(
            repository.hasOutstandingBookLoaned(operation.getUserId()),
            "User has outstanding loaned books, he cannot borrowed any more until all books returned");

        log.info("Before loan, checking if user can get more books based on the limit of number of books loaned...");
        RestPreConditions.checkState(
            repository.countOpenLoanedByUserId(operation.getUserId()) >= borrowLimitParam.getValue(),
            "User cannot borrowed more than %s books", borrowLimitParam.getValue());

        log.info("Building the entity...");
        final BookOrderEntity entity = BookOrderEntity.builder()
            .bookId(operation.getBookId())
            .userId(operation.getUserId())
            .loanedAt(LocalDateTime.now())
            .returnIn(LocalDateTime.now().plusDays(borrowDaysParam.getValue()))
            .build();

        log.info("Confirming and save the loan...");
        return repository.save(entity);
    }

    public BookOrderEntity returned(OperationDTO operation) {
        log.info("Checking if the loan exists in the database...");
        BookOrderEntity entity = repository.findOpenLoanedByUserIdAndBookId(operation.getUserId(), operation.getBookId())
            .orElseThrow(() -> new DataNotFoundException("Loan for this book not found"));

        entity.setReturnedAt(LocalDateTime.now());

        log.info("Confirm return and save to database...");
        return repository.save(entity);
    }
}
