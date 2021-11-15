package com.orderservice.business.service;

import com.orderservice.business.persistence.entity.BookOrderEntity;
import com.orderservice.business.persistence.repository.BookOrderRepository;
import com.orderservice.business.service.client.ParamServiceClient;
import com.orderservice.business.service.properties.BookOrderProperties;
import com.orderservice.commons.exception.BadRequestException;
import com.orderservice.commons.exception.DataNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static com.orderservice.business.web.controller.utils.BookOrderServiceUtils.getBorrowDaysParam;
import static com.orderservice.business.web.controller.utils.BookOrderServiceUtils.getBorrowLimitParam;
import static com.orderservice.business.web.controller.utils.BookOrderServiceUtils.getLoanReturnOperation;
import static com.orderservice.business.web.controller.utils.BookOrderServiceUtils.getLoanOrder;
import static com.orderservice.business.web.controller.utils.TestUtils.getRandomLong;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookOrderServiceTest {

    @InjectMocks
    private BookOrderService bookOrderService;

    @Mock
    private BookOrderRepository repository;

    @Mock
    private ParamServiceClient paramClient;

    private BookOrderProperties properties;

    @Captor
    private ArgumentCaptor<BookOrderEntity> bookOrderArgumentCaptor;

    @BeforeEach
    void setup() {
        properties = new BookOrderProperties("borrow-limit", "borrow-days");
        ReflectionTestUtils.setField(bookOrderService, "properties", properties);
    }

    @Test
    public void loanBook_shouldLoanBook() {

        final var bookId = getRandomLong();
        final var userId = getRandomLong();
        final var operation = getLoanReturnOperation(bookId, userId);
        final var outBookOrder = getLoanOrder(bookId, userId);
        final var borrowLimitParam = getBorrowLimitParam(properties.getBorrowLimitKey());
        final var borrowDaysKey = getBorrowDaysParam(properties.getBorrowDaysKey());

        when(paramClient.findByKey(properties.getBorrowLimitKey())).thenReturn(borrowLimitParam);
        when(paramClient.findByKey(properties.getBorrowDaysKey())).thenReturn(borrowDaysKey);
        when(repository.hasBookLoaned(operation.getBookId())).thenReturn(false);
        when(repository.hasOutstandingBookLoaned(operation.getUserId())).thenReturn(false);
        when(repository.countOpenLoanedByUserId(operation.getUserId())).thenReturn(2);
        when(repository.save(bookOrderArgumentCaptor.capture())).thenReturn(outBookOrder);

        final var result = bookOrderService.loaned(operation);

        assertEquals(outBookOrder, result);

        final var argumentCaptorValue = bookOrderArgumentCaptor.getValue();

        assertNotNull(argumentCaptorValue.getBookId());
        assertNotNull(argumentCaptorValue.getUserId());
        assertNotNull(argumentCaptorValue.getLoanedAt());
        assertNotNull(argumentCaptorValue.getReturnIn());
        assertNull(argumentCaptorValue.getReturnedAt());

        verify(repository, times(1)).hasBookLoaned(operation.getBookId());
        verify(repository, times(1)).hasOutstandingBookLoaned(operation.getUserId());
        verify(repository, times(1)).countOpenLoanedByUserId(operation.getUserId());
        verify(repository, times(1)).save(argumentCaptorValue);
    }

    @Test
    public void loanBook_shouldNotCreate_BookAlreadyHadLoaned() {

        final var bookId = getRandomLong();
        final var userId = getRandomLong();
        final var operation = getLoanReturnOperation(bookId, userId);
        final var borrowLimitParam = getBorrowLimitParam(properties.getBorrowLimitKey());
        final var borrowDaysKey = getBorrowDaysParam(properties.getBorrowDaysKey());

        when(paramClient.findByKey(properties.getBorrowLimitKey())).thenReturn(borrowLimitParam);
        when(paramClient.findByKey(properties.getBorrowDaysKey())).thenReturn(borrowDaysKey);
        when(repository.hasBookLoaned(operation.getBookId())).thenReturn(true);

        final var assertThrows = assertThrows(BadRequestException.class, () -> bookOrderService.loaned(operation));
        assertEquals("Book is not available to loan", assertThrows.getMessage());
        verify(repository, times(1)).hasBookLoaned(operation.getBookId());
        verify(repository, times(0)).hasOutstandingBookLoaned(operation.getUserId());
        verify(repository, times(0)).countOpenLoanedByUserId(operation.getUserId());
        verify(repository, times(0)).save(any(BookOrderEntity.class));
    }

    @Test
    public void loanBook_shouldNotCreate_UserHasOutstandingBooksLoaned() {

        final var bookId = getRandomLong();
        final var userId = getRandomLong();
        final var operation = getLoanReturnOperation(bookId, userId);
        final var outBookOrder = getLoanOrder(bookId, userId);
        final var borrowLimitParam = getBorrowLimitParam(properties.getBorrowLimitKey());
        final var borrowDaysKey = getBorrowDaysParam(properties.getBorrowDaysKey());

        when(paramClient.findByKey(properties.getBorrowLimitKey())).thenReturn(borrowLimitParam);
        when(paramClient.findByKey(properties.getBorrowDaysKey())).thenReturn(borrowDaysKey);
        when(repository.hasBookLoaned(operation.getBookId())).thenReturn(false);
        when(repository.hasOutstandingBookLoaned(operation.getUserId())).thenReturn(true);

        final var assertThrows = assertThrows(BadRequestException.class, () -> bookOrderService.loaned(operation));
        assertEquals("User has outstanding loaned books, he cannot borrowed any more until all books returned", assertThrows.getMessage());
        verify(repository, times(1)).hasBookLoaned(operation.getBookId());
        verify(repository, times(1)).hasOutstandingBookLoaned(operation.getUserId());
        verify(repository, times(0)).countOpenLoanedByUserId(operation.getUserId());
        verify(repository, times(0)).save(any(BookOrderEntity.class));
    }

    @Test
    public void loanBook_shouldNotCreate_UserCanNotBorrowMoreBookThanLimit() {

        final var bookId = getRandomLong();
        final var userId = getRandomLong();
        final var operation = getLoanReturnOperation(bookId, userId);
        final var outBookOrder = getLoanOrder(bookId, userId);
        final var borrowLimitParam = getBorrowLimitParam(properties.getBorrowLimitKey());
        final var borrowDaysKey = getBorrowDaysParam(properties.getBorrowDaysKey());

        when(paramClient.findByKey(properties.getBorrowLimitKey())).thenReturn(borrowLimitParam);
        when(paramClient.findByKey(properties.getBorrowDaysKey())).thenReturn(borrowDaysKey);
        when(repository.hasBookLoaned(operation.getBookId())).thenReturn(false);
        when(repository.hasOutstandingBookLoaned(operation.getUserId())).thenReturn(false);
        when(repository.countOpenLoanedByUserId(operation.getUserId())).thenReturn(3);

        final var assertThrows = assertThrows(BadRequestException.class, () -> bookOrderService.loaned(operation));
        assertEquals("User cannot borrowed more than %s books".formatted(borrowLimitParam.getValue()), assertThrows.getMessage());
        verify(repository, times(1)).hasBookLoaned(operation.getBookId());
        verify(repository, times(1)).hasOutstandingBookLoaned(operation.getUserId());
        verify(repository, times(1)).countOpenLoanedByUserId(operation.getUserId());
        verify(repository, times(0)).save(any(BookOrderEntity.class));
    }

    @Test
    public void returnBook_shouldReturnBook() {

        final var bookId = getRandomLong();
        final var userId = getRandomLong();
        final var operation = getLoanReturnOperation(bookId, userId);
        final var outBookOrder = getLoanOrder(bookId, userId);

        when(repository.findOpenLoanedByUserIdAndBookId(operation.getUserId(), operation.getBookId()))
            .thenReturn(Optional.of(outBookOrder));
        when(repository.save(bookOrderArgumentCaptor.capture())).thenReturn(outBookOrder);

        final var result = bookOrderService.returned(operation);

        assertEquals(outBookOrder, result);

        final var argumentCaptorValue = bookOrderArgumentCaptor.getValue();

        assertNotNull(argumentCaptorValue.getBookId());
        assertNotNull(argumentCaptorValue.getUserId());
        assertNotNull(argumentCaptorValue.getLoanedAt());
        assertNotNull(argumentCaptorValue.getReturnIn());
        assertNotNull(argumentCaptorValue.getReturnedAt());

        verify(repository, times(1)).findOpenLoanedByUserIdAndBookId(operation.getUserId(), operation.getBookId());
        verify(repository, times(1)).save(argumentCaptorValue);
    }

    @Test
    public void returnBook_shouldNotReturn_LoanNotFound() {

        final var bookId = getRandomLong();
        final var userId = getRandomLong();
        final var operation = getLoanReturnOperation(bookId, userId);

        when(repository.findOpenLoanedByUserIdAndBookId(operation.getUserId(), operation.getBookId()))
            .thenReturn(Optional.empty());

        final var assertThrows = assertThrows(DataNotFoundException.class, () -> bookOrderService.returned(operation));
        assertEquals("Loan for this book not found", assertThrows.getMessage());

        verify(repository, times(1)).findOpenLoanedByUserIdAndBookId(operation.getUserId(), operation.getBookId());
        verify(repository, times(0)).save(any(BookOrderEntity.class));
    }
}
