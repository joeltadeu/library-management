package com.orderservice.business.web.controller;

import com.orderservice.Application;
import com.orderservice.business.service.BookOrderService;
import com.orderservice.business.web.dto.OperationDTO;
import com.orderservice.business.web.helper.BookOrderHelper;
import com.orderservice.commons.exception.BadRequestException;
import com.orderservice.infrastructure.config.ModelMapperConfig;
import com.orderservice.infrastructure.config.SleuthConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static com.orderservice.business.web.controller.utils.BookOrderControllerUtils.getBookOrderEntity;
import static com.orderservice.business.web.controller.utils.BookOrderControllerUtils.getLoanReturnBookRequestJson;
import static com.orderservice.business.web.controller.utils.BookOrderControllerUtils.getLoanReturnBookRequestWithoutBookIdJson;
import static com.orderservice.business.web.controller.utils.BookOrderControllerUtils.getLoanReturnBookRequestWithoutUserIdJson;
import static com.orderservice.business.web.controller.utils.TestUtils.getRandomLong;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest({BookOrderController.class})
@ContextConfiguration(classes = { Application.class, SleuthConfig.class, ModelMapperConfig.class, BookOrderHelper.class })
public class BookOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookOrderService bookOrderServiceMock;

    @Test
    public void loanBook_shouldLoanBook() throws Exception {
        final var bookId = getRandomLong();
        final var userId = getRandomLong();
        final var orderId = getRandomLong();
        final var requestJson = getLoanReturnBookRequestJson(bookId, userId);
        final var bookOrder = getBookOrderEntity(orderId, bookId, userId);

        when(bookOrderServiceMock.loaned(any(OperationDTO.class))).thenReturn(bookOrder);

        mockMvc.perform(post("/api/v1/loan").contentType(APPLICATION_JSON)
                .content( requestJson))
            .andExpect(status().is(201))
            .andExpect(jsonPath("id").value(orderId));

        verify(bookOrderServiceMock, times(1)).loaned(any(OperationDTO.class));
    }

    @Test
    public void loanBook_shouldBadRequestWhenMissingBookId() throws Exception {

        final var requestJson = getLoanReturnBookRequestWithoutBookIdJson();

        mockMvc.perform(post("/api/v1/loan").contentType(APPLICATION_JSON)
                .content( requestJson))
            .andExpect(status().is(400))
            .andExpect(jsonPath("status").value("Bad Request"))
            .andExpect(jsonPath("description").value("Validation Exception"))
            .andExpect(jsonPath("attributes[0].attribute").value("bookId"))
            .andExpect(jsonPath("attributes[0].errors[0]").value("Book was not informed"));;
    }

    @Test
    public void loanBook_shouldBadRequestWhenMissingUserId() throws Exception {

        final var requestJson = getLoanReturnBookRequestWithoutUserIdJson();

        mockMvc.perform(post("/api/v1/loan").contentType(APPLICATION_JSON)
                .content( requestJson))
            .andExpect(status().is(400))
            .andExpect(jsonPath("status").value("Bad Request"))
            .andExpect(jsonPath("description").value("Validation Exception"))
            .andExpect(jsonPath("attributes[0].attribute").value("userId"))
            .andExpect(jsonPath("attributes[0].errors[0]").value("User was not informed"));;
    }

    @Test
    public void loanBook_shouldNotCreate_BookAlreadyHadLoaned() throws Exception {

        final var bookId = getRandomLong();
        final var userId = getRandomLong();
        final var requestJson = getLoanReturnBookRequestJson(bookId, userId);

        when(bookOrderServiceMock.loaned(any(OperationDTO.class)))
            .thenThrow(new BadRequestException(("Book is not available to loan")));

        mockMvc.perform(post("/api/v1/loan").contentType(APPLICATION_JSON)
                .content( requestJson))
            .andExpect(status().is(400))
            .andExpect(jsonPath("status").value("Bad Request"))
            .andExpect(jsonPath("description").value("Book is not available to loan"));
    }

    @Test
    public void loanBook__shouldNotCreate_UserHasOutstandingBooksLoaned() throws Exception {

        final var bookId = getRandomLong();
        final var userId = getRandomLong();
        final var requestJson = getLoanReturnBookRequestJson(bookId, userId);

        when(bookOrderServiceMock.loaned(any(OperationDTO.class)))
            .thenThrow(new BadRequestException(("User has outstanding loaned books, he cannot borrowed any more until all books returned")));

        mockMvc.perform(post("/api/v1/loan").contentType(APPLICATION_JSON)
                .content( requestJson))
            .andExpect(status().is(400))
            .andExpect(jsonPath("status").value("Bad Request"))
            .andExpect(jsonPath("description").value("User has outstanding loaned books, he cannot borrowed any more until all books returned"));
    }

    @Test
    public void loanBook__shouldNotCreate_UserCanNotBorrowMoreBookThanLimit() throws Exception {

        final var bookId = getRandomLong();
        final var userId = getRandomLong();
        final var requestJson = getLoanReturnBookRequestJson(bookId, userId);

        when(bookOrderServiceMock.loaned(any(OperationDTO.class)))
            .thenThrow(new BadRequestException(("User cannot borrowed more than 3 books")));

        mockMvc.perform(post("/api/v1/loan").contentType(APPLICATION_JSON)
                .content( requestJson))
            .andExpect(status().is(400))
            .andExpect(jsonPath("status").value("Bad Request"))
            .andExpect(jsonPath("description").value("User cannot borrowed more than 3 books"));
    }

    @Test
    public void returnBook_shouldReturnBook() throws Exception {
        final var bookId = getRandomLong();
        final var userId = getRandomLong();
        final var orderId = getRandomLong();
        final var returnedAt = LocalDateTime.now();
        final var requestJson = getLoanReturnBookRequestJson(bookId, userId);
        final var bookOrder = getBookOrderEntity(orderId, bookId, userId, returnedAt);

        when(bookOrderServiceMock.returned(any(OperationDTO.class))).thenReturn(bookOrder);

        mockMvc.perform(post("/api/v1/return").contentType(APPLICATION_JSON)
                .content( requestJson))
            .andExpect(status().is(200))
            .andExpect(jsonPath("returnedAt").exists());

        verify(bookOrderServiceMock, times(1)).returned(any(OperationDTO.class));
    }

    @Test
    public void returnBook__shouldNotReturn_LoanNotFound() throws Exception {

        final var bookId = getRandomLong();
        final var userId = getRandomLong();
        final var requestJson = getLoanReturnBookRequestJson(bookId, userId);

        when(bookOrderServiceMock.returned(any(OperationDTO.class)))
            .thenThrow(new BadRequestException(("Loan for this book not found")));

        mockMvc.perform(post("/api/v1/return").contentType(APPLICATION_JSON)
                .content( requestJson))
            .andExpect(status().is(400))
            .andExpect(jsonPath("status").value("Bad Request"))
            .andExpect(jsonPath("description").value("Loan for this book not found"));
    }
}
