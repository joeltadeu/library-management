package com.bookservice.business.service;

import com.bookservice.business.persistence.entity.AuthorEntity;
import com.bookservice.business.persistence.entity.BookEntity;
import com.bookservice.business.persistence.entity.CategoryEntity;
import com.bookservice.business.persistence.repository.BookCriteriaRepository;
import com.bookservice.business.persistence.repository.BookRepository;
import com.bookservice.commons.exception.BadRequestException;
import com.bookservice.commons.exception.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;

import static com.bookservice.business.web.controller.utils.BookServiceUtils.getBookEntity;
import static com.bookservice.business.web.controller.utils.BookServiceUtils.getBookPageable;
import static com.bookservice.business.web.controller.utils.BookServiceUtils.getBookSearchCriteria;
import static com.bookservice.business.web.controller.utils.BookServiceUtils.getPageBook;
import static com.bookservice.business.web.controller.utils.TestUtils.getRandomLong;
import static com.bookservice.business.web.controller.utils.TestUtils.getRandomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepositoryMock;

    @Mock
    private BookCriteriaRepository bookCriteriaRepository;

    @InjectMocks
    private BookService bookService;

    @Captor
    private ArgumentCaptor<BookEntity> bookArgumentCaptor;

    @Test
    public void createBook_shouldCreateBook() {
        final var bookTitle = getRandomString();
        final var bookIsbn = getRandomString();
        final var bookId = getRandomLong();
        final var authorId = getRandomLong();
        final var author = AuthorEntity.builder().id(authorId).build();
        final var categoryId = getRandomLong();
        final var categories = Collections.singleton(CategoryEntity.builder().id(categoryId).build());

        final var incomeBook = new BookEntity(null, bookIsbn, author, bookTitle, categories);
        final var outBook = new BookEntity(bookId, bookIsbn, author, bookTitle, categories);

        when(bookRepositoryMock.findByIsbn(incomeBook.getIsbn())).thenReturn(Optional.empty());
        when(bookRepositoryMock.save(bookArgumentCaptor.capture())).thenReturn(outBook);

        final var result = bookService.insert(incomeBook);

        assertEquals(outBook, result);

        final var argumentCaptorValue = bookArgumentCaptor.getValue();

        assertEquals(incomeBook.getTitle(), argumentCaptorValue.getTitle());
        assertEquals(incomeBook.getIsbn(), argumentCaptorValue.getIsbn());
        assertEquals(incomeBook.getAuthor(), argumentCaptorValue.getAuthor());
        assertEquals(incomeBook.getCategories(), argumentCaptorValue.getCategories());
        assertNull(argumentCaptorValue.getId());

        verify(bookRepositoryMock, times(1)).findByIsbn(incomeBook.getIsbn());
        verify(bookRepositoryMock, times(1)).save(argumentCaptorValue);
    }

    @Test
    public void createBook_shouldNotCreateWhenIsbnAlreadyExists() {
        final var bookTitle = getRandomString();
        final var bookIsbn = getRandomString();
        final var authorId = getRandomLong();
        final var author = AuthorEntity.builder().id(authorId).build();
        final var categoryId = getRandomLong();
        final var categories = Collections.singleton(CategoryEntity.builder().id(categoryId).build());
        final var incomeBook = new BookEntity(null, bookIsbn, author, bookTitle, categories);

        when(bookRepositoryMock.findByIsbn(bookIsbn)).thenReturn(Optional.of(new BookEntity()));

        final var assertThrows = assertThrows(BadRequestException.class, () -> bookService.insert(incomeBook));
        assertEquals("There is another book using the same isbn '%s' informed".formatted(bookIsbn), assertThrows.getMessage());
        verify(bookRepositoryMock, times(1)).findByIsbn(bookIsbn);
    }

    @Test
    public void updateBook_shouldUpdateBook() {
        final var bookId = getRandomLong();
        final var bookTitle = getRandomString();
        final var bookIsbn = getRandomString();
        final var authorId = getRandomLong();
        final var author = AuthorEntity.builder().id(authorId).build();
        final var categoryId = getRandomLong();
        final var categories = Collections.singleton(CategoryEntity.builder().id(categoryId).build());
        final var incomeBook = new BookEntity(null, bookIsbn, author, bookTitle, categories);

        when(bookRepositoryMock.existsById(bookId)).thenReturn(true);

        bookService.update(incomeBook, bookId);

        verify(bookRepositoryMock, times(1)).save(incomeBook);
        verify(bookRepositoryMock, times(1)).existsById(bookId);
    }

    @Test
    public void updateBook_shouldNotUpdateWhenBookWasNotFound() {
        final var bookId = getRandomLong();
        final var bookTitle = getRandomString();
        final var bookIsbn = getRandomString();
        final var authorId = getRandomLong();
        final var author = AuthorEntity.builder().id(authorId).build();
        final var categoryId = getRandomLong();
        final var categories = Collections.singleton(CategoryEntity.builder().id(categoryId).build());
        final var incomeBook = new BookEntity(null, bookIsbn, author, bookTitle, categories);

        when(bookRepositoryMock.existsById(bookId)).thenReturn(false);

        final var assertThrows = assertThrows(DataNotFoundException.class, () -> bookService.update(incomeBook, bookId));
        assertEquals("Book with Id %s was not found".formatted(bookId), assertThrows.getMessage());
        verify(bookRepositoryMock, times(1)).existsById(bookId);
        verify(bookRepositoryMock, times(0)).save(any(BookEntity.class));
    }

    @Test
    public void deleteBookById_shouldDelete() {
        final var bookId = getRandomLong();
        final var foundBook = getBookEntity(bookId);

        when(bookRepositoryMock.findById(bookId)).thenReturn(Optional.of(foundBook));

        doNothing().when(bookRepositoryMock).delete(foundBook);

        bookService.delete(bookId);

        verify(bookRepositoryMock, times(1)).findById(bookId);
        verify(bookRepositoryMock, times(1)).delete(foundBook);
    }

    @Test
    public void deleteBookById_shouldNotFound() {
        final var bookId = getRandomLong();
        when(bookRepositoryMock.findById(bookId)).thenReturn(Optional.empty());

        final var assertThrows = assertThrows(DataNotFoundException.class, () -> bookService.delete(bookId));

        assertEquals("Book with Id %s was not found".formatted(bookId), assertThrows.getMessage());

        verify(bookRepositoryMock, times(1)).findById(bookId);
        verify(bookRepositoryMock, times(0)).delete(any());
    }

    @Test
    public void findBookById_shouldFind() {
        final var bookId = getRandomLong();
        final var returnedBook = getBookEntity(bookId);

        when(bookRepositoryMock.findById(bookId)).thenReturn(Optional.of(returnedBook));

        final var bookReturnedFromDatabase = bookService.findById(bookId);

        assertEquals(returnedBook, bookReturnedFromDatabase);

        verify(bookRepositoryMock, times(1)).findById(bookId);
    }

    @Test
    public void findBookById_shouldNotFind() {
        final var bookId = getRandomLong();

        when(bookRepositoryMock.findById(bookId)).thenReturn(Optional.empty());

        final var assertThrows = assertThrows(DataNotFoundException.class, () -> bookService.findById(bookId));

        assertEquals("Book with Id %s was not found".formatted(bookId), assertThrows.getMessage());

        verify(bookRepositoryMock, times(1)).findById(bookId);
    }

    @Test
    public void findAllBooks_shouldFind() {
        final var pageable = getBookPageable();
        final var bookCriteria = getBookSearchCriteria();
        final var returnedPage = getPageBook(pageable);

        when(bookCriteriaRepository.findAllWithFilters(pageable, bookCriteria)).thenReturn(returnedPage);

        final var pageBook =  bookService.findAll(pageable, bookCriteria);

        assertNotNull(pageBook.getTotalElements());
        assertNotNull(pageBook.getTotalPages());
        assertNotNull(pageBook.getSize());
        assertNotNull(pageBook.getContent());
        assertNotNull(pageBook.getNumber());
        assertNotNull(pageBook.getNumberOfElements());

        verify(bookCriteriaRepository, times(1)).findAllWithFilters(pageable, bookCriteria);
    }
}
