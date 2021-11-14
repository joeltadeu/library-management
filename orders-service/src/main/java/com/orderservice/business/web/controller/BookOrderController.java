package com.orderservice.business.web.controller;

import com.orderservice.business.persistence.entity.BookOrderEntity;
import com.orderservice.business.service.BookOrderService;
import com.orderservice.business.web.dto.BookOrderDTO;
import com.orderservice.business.web.dto.OperationDTO;
import com.orderservice.business.web.helper.BookOrderHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1")
public class BookOrderController {

    private final BookOrderService service;
    private final BookOrderHelper helper;

    public BookOrderController(BookOrderService service, BookOrderHelper helper) {
        this.service = service;
        this.helper = helper;
    }

    @PostMapping(value="/loan", produces="application/json", consumes="application/json")
    @Operation(summary = "Loan a book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Book loaned successfully")
    })
    public ResponseEntity<BookOrderDTO> loaned(@RequestBody @Valid OperationDTO operation) {
        log.info("Book to borrow: [{}]", operation);
        BookOrderEntity saved = service.loaned(operation);
        log.info("Book borrowed: [{}]", saved);
        return new ResponseEntity<>(helper.toModel(saved), HttpStatus.CREATED);
    }

    @PostMapping(value="/return", produces="application/json", consumes="application/json")
    @Operation(summary = "Return a book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book returned successfully")
    })
    public ResponseEntity<BookOrderDTO> returned(@RequestBody @Valid OperationDTO operation) {
        log.info("Book to return: [{}]", operation);
        BookOrderEntity saved = service.returned(operation);
        log.info("Book returned: [{}]", saved);
        return new ResponseEntity<>(helper.toModel(saved), HttpStatus.OK);
    }
}
