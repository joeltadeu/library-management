package com.bookservice.business.web.controller;

import com.bookservice.Application;
import com.bookservice.business.persistence.entity.AuthorEntity;
import com.bookservice.business.persistence.entity.BookEntity;
import com.bookservice.business.persistence.entity.CategoryEntity;
import com.bookservice.business.service.BookService;
import com.bookservice.business.web.helper.BookHelper;
import com.bookservice.commons.exception.BadRequestException;
import com.bookservice.commons.exception.DataNotFoundException;
import com.bookservice.infrastructure.config.ModelMapperConfig;
import com.bookservice.infrastructure.config.SleuthConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static com.bookservice.business.web.controller.utils.BookControllerUtils.getCreateBookRequestJson;
import static com.bookservice.business.web.controller.utils.BookControllerUtils.getCreateBookRequestWithoutAuthorJson;
import static com.bookservice.business.web.controller.utils.BookControllerUtils.getCreateBookRequestWithoutIsbnJson;
import static com.bookservice.business.web.controller.utils.BookControllerUtils.getCreateBookRequestWithoutTitleJson;
import static com.bookservice.business.web.controller.utils.BookControllerUtils.getUpdateBookRequestJson;
import static com.bookservice.business.web.controller.utils.TestUtils.getRandomLong;
import static com.bookservice.business.web.controller.utils.TestUtils.getRandomString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(SpringExtension.class)
@WebMvcTest({ BookController.class })
@ContextConfiguration(classes = { Application.class, SleuthConfig.class, ModelMapperConfig.class, BookHelper.class })
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookServiceMock;

    @Test
    public void createBook_shouldCreate() throws Exception {

        final var bookTitle = getRandomString();
        final var bookIsbn = getRandomString();
        final var bookId = getRandomLong();
        final var authorId = getRandomLong();
        final var author = AuthorEntity.builder().id(authorId).build();
        final var categoryId = getRandomLong();
        final var categories = Collections.singleton(CategoryEntity.builder().id(categoryId).build());
        final var requestJson = getCreateBookRequestJson(bookTitle, bookIsbn, authorId, categoryId);
        final var book = new BookEntity(bookId, bookIsbn, author, bookTitle, categories);

        when(bookServiceMock.insert(any(BookEntity.class))).thenReturn(book);

        mockMvc.perform(post("/api/v1/books").contentType(APPLICATION_JSON)
                .content( requestJson))
            .andExpect(status().is(201))
            .andExpect(jsonPath("id").value(bookId));

        verify(bookServiceMock, times(1)).insert(any(BookEntity.class));
    }

    @Test
    public void createBook_shouldBadRequestWhenMissingTitle() throws Exception {

        final var requestJson = getCreateBookRequestWithoutTitleJson();

        mockMvc.perform(post("/api/v1/books").contentType(APPLICATION_JSON)
            .content( requestJson))
            .andExpect(status().is(400))
            .andExpect(jsonPath("status").value("Bad Request"))
            .andExpect(jsonPath("description").value("Validation Exception"))
            .andExpect(jsonPath("attributes[0].attribute").value("title"))
            .andExpect(jsonPath("attributes[0].errors[0]").value("Title was not informed"));;
    }

    @Test
    public void createBook_shouldBadRequestWhenMissingIsbn() throws Exception {

        final var requestJson = getCreateBookRequestWithoutIsbnJson();

        mockMvc.perform(post("/api/v1/books").contentType(APPLICATION_JSON)
                .content( requestJson))
            .andExpect(status().is(400))
            .andExpect(jsonPath("status").value("Bad Request"))
            .andExpect(jsonPath("description").value("Validation Exception"))
            .andExpect(jsonPath("attributes[0].attribute").value("isbn"))
            .andExpect(jsonPath("attributes[0].errors[0]").value("ISBN was not informed"));;
    }

    @Test
    public void createBook_shouldBadRequestWhenMissingAuthor() throws Exception {

        final var requestJson = getCreateBookRequestWithoutAuthorJson();

        mockMvc.perform(post("/api/v1/books").contentType(APPLICATION_JSON)
                .content( requestJson ))
            .andExpect(status().is(400))
            .andExpect(jsonPath("status").value("Bad Request"))
            .andExpect(jsonPath("description").value("Validation Exception"))
            .andExpect(jsonPath("attributes[0].attribute").value("authorId"))
            .andExpect(jsonPath("attributes[0].errors[0]").value("Author was not informed"));;
    }

    @Test
    public void createBook_shouldBadRequestWhenIsbnAlreadyExists() throws Exception {

        final var bookTitle = getRandomString();
        final var bookIsbn = getRandomString();
        final var authorId = getRandomLong();
        final var categoryId = getRandomLong();
        final var requestJson = getCreateBookRequestJson(bookTitle, bookIsbn, authorId, categoryId);

        when(bookServiceMock.insert(any(BookEntity.class))).thenThrow(new BadRequestException("There is another book using the same isbn '%s' informed".formatted(bookIsbn)));

        mockMvc.perform(post("/api/v1/books").contentType(APPLICATION_JSON)
                .content( requestJson))
            .andExpect(status().is(400))
            .andExpect(jsonPath("status").value("Bad Request"))
            .andExpect(jsonPath("description").value("There is another book using the same isbn '%s' informed".formatted(bookIsbn)));
    }

    @Test
    public void updateBook_shouldUpdate() throws Exception {

        final var bookId = getRandomLong();
        final var requestJson = getUpdateBookRequestJson();

        mockMvc.perform(put("/api/v1/books/{id}", bookId).contentType(APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().is(200));

        verify(bookServiceMock, times(1)).update(any(BookEntity.class), anyLong());
    }

    @Test
    public void findBookById_shouldFind() throws Exception {

        final var bookTitle = getRandomString();
        final var bookIsbn = getRandomString();
        final var bookId = getRandomLong();
        final var authorId = getRandomLong();
        final var authorName = getRandomString();
        final var author = new AuthorEntity(authorId, authorName);
        final var categoryId = getRandomLong();
        final var categoryName = getRandomString();
        final var categories = Collections.singleton(new CategoryEntity(categoryId, categoryName));
        final var book = new BookEntity(bookId, bookIsbn, author, bookTitle, categories);

        when(bookServiceMock.findById(bookId)).thenReturn(book);

        mockMvc.perform(get("/api/v1/books/{id}", bookId).contentType(APPLICATION_JSON))
            .andExpect(status().is(200))
            .andExpect(jsonPath("title").value(bookTitle))
            .andExpect(jsonPath("isbn").value(bookIsbn))
            .andExpect(jsonPath("author").value(author))
            .andExpect(jsonPath("id").value(bookId))
            .andExpect(jsonPath("categories[0].id").value(categoryId))
            .andExpect(jsonPath("categories[0].name").value(categoryName));

        verify(bookServiceMock, times(1)).findById(bookId);
    }

    @Test
    public void findBookById_shouldThrowNotFoundWhenNotFound() throws Exception {
        final var bookId = getRandomLong();
        when(bookServiceMock.findById(bookId)).thenThrow(
            new DataNotFoundException("Book with Id %s was not found".formatted(bookId)));

        mockMvc.perform(get("/api/v1/books/{id}", bookId).contentType(APPLICATION_JSON))
            .andExpect(status().is(404))
            .andExpect(jsonPath("description").value("Book with Id %s was not found".formatted(bookId)));

        verify(bookServiceMock, times(1)).findById(bookId);
    }

    @Test
    public void deleteBook_shouldDeleteBook() throws Exception {
        final var bookId = getRandomLong();
        doNothing().when(bookServiceMock).delete(bookId);

        mockMvc.perform(delete("/api/v1/books/{id}", bookId).contentType(APPLICATION_JSON))
            .andExpect(status().is(204));

        verify(bookServiceMock, times(1)).delete(bookId);
    }

    @Test
    public void deleteBook_shouldThrowNotFoundWhenNotFound() throws Exception {
        final var bookId = getRandomLong();
        doThrow(new DataNotFoundException("Book with Id %s was not found".formatted(bookId)))
            .when(bookServiceMock)
            .delete(bookId);

        mockMvc.perform(delete("/api/v1/books/{id}", bookId).contentType(APPLICATION_JSON))
            .andExpect(status().is(404))
            .andExpect(jsonPath("description").value("Book with Id %s was not found".formatted(bookId)));

        verify(bookServiceMock, times(1)).delete(bookId);
    }

}
