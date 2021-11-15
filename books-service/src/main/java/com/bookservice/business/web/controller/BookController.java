package com.bookservice.business.web.controller;

import com.bookservice.business.service.BookService;
import com.bookservice.business.web.dto.BookInsertUpdateDTO;
import com.bookservice.business.persistence.entity.BookEntity;
import com.bookservice.business.web.dto.BookDTO;
import com.bookservice.business.web.dto.criteria.BookSearchCriteria;
import com.bookservice.business.web.dto.pagination.BookPage;
import com.bookservice.business.web.helper.BookHelper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/books")
public class BookController {

    private final BookService service;
    private final BookHelper helper;

    public BookController(BookService service, BookHelper helper) {
        this.service = service;
        this.helper = helper;
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    @Operation(summary = "Get a book by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the book",
                     content = { @Content(mediaType = "application/json",
                                          schema = @Schema(implementation = BookEntity.class)) }),
        @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                     content = @Content),
        @ApiResponse(responseCode = "404", description = "Book not found",
                     content = @Content) })
    public ResponseEntity<BookDTO> find(@Parameter(description = "id of book to be searched")
                                                 @PathVariable Long id) {
        log.info("Finding book by id: {}", id);
        BookEntity book = service.findById(id);
        log.info("Book found: [{}]", book);
        return ResponseEntity.ok(helper.toModel(book));
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Get list of books")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of books")
    })
    public ResponseEntity<Page<BookDTO>> findAll(BookPage page, BookSearchCriteria criteria){
        Page<BookEntity> bookPage = service.findAll(page, criteria);
        log.info("Getting the list of books based on criteria: [{}]", criteria);
        return new ResponseEntity<>(helper.toModel(bookPage), HttpStatus.OK);
    }

    @PostMapping(produces="application/json", consumes="application/json")
    @Operation(summary = "Insert a book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Book inserted successfully")
    })
    public ResponseEntity<BookDTO> insert(@RequestBody
                                               @Valid BookInsertUpdateDTO book) {
        log.info("Saving a new the book: [{}]", book);
        BookEntity saved = service.insert(helper.toEntity(book));
        log.info("Book saved: [{}]", saved);
        return new ResponseEntity<>(helper.toModel(saved), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", produces="application/json")
    @Operation(summary = "Update a book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Book updated successfully")
    })
    public ResponseEntity<Void> update(@RequestBody @Valid BookInsertUpdateDTO book,
                                       @Parameter(name = "Book Id", example = "12", required = true)
                                       @PathVariable Long id) {
        log.info("Updating a book: [{}] with id: {}", book, id);
        service.update(helper.toEntity(book), id);
        log.info("Book saved...");
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a book")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Book deleted successfully")
    })
    public ResponseEntity<Void> delete(@Parameter(name = "Book Id",required = true) @PathVariable Long id) {
        log.info("Deleting a book with id: {}", id);
        service.delete(id);
        log.info("Booking deleted...");
        return ResponseEntity.noContent().build();
    }
}