package com.bookservice.business.web.controller.utils;

import com.bookservice.business.persistence.entity.AuthorEntity;
import com.bookservice.business.persistence.entity.BookEntity;
import com.bookservice.business.persistence.entity.CategoryEntity;
import com.bookservice.business.web.dto.criteria.BookSearchCriteria;
import com.bookservice.business.web.dto.pagination.BookPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static com.bookservice.business.web.controller.utils.TestUtils.getRandomLong;
import static com.bookservice.business.web.controller.utils.TestUtils.getRandomString;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BookServiceUtils {

    public static BookEntity getBookEntity() {
        return getBookEntity(getRandomLong());
    }

    public static BookEntity getBookEntity(Long bookId) {
        final var bookTitle = getRandomString();
        final var bookIsbn = getRandomString();
        final var authorId = getRandomLong();
        final var author = AuthorEntity.builder().id(authorId).build();
        final var categoryId = getRandomLong();
        final var categories = Collections.singleton(CategoryEntity.builder().id(categoryId).build());
        return new BookEntity(bookId, bookIsbn, author, bookTitle, categories);
    }

    public static BookPage getBookPageable() {
        return new BookPage();
    }

    public static BookSearchCriteria getBookSearchCriteria() {
        return new BookSearchCriteria();
    }

    public static Page<BookEntity> getPageBook(BookPage page) {
        final var books = getBooksList();
        final var sort = Sort.by(page.getSortDirection(), page.getSortBy());
        final var pageable = PageRequest.of(page.getPageNumber(), page.getPageSize(), sort);
        return new PageImpl<>(books, pageable, books.size());
    }

    public static List<BookEntity> getBooksList() {
        return Arrays.asList(getBookEntity(), getBookEntity());
    }
}
