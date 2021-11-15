package com.bookservice.business.service;

import com.bookservice.commons.exception.DataNotFoundException;
import com.bookservice.commons.validation.RestPreConditions;
import com.bookservice.business.persistence.entity.BookEntity;
import com.bookservice.business.persistence.repository.BookCriteriaRepository;
import com.bookservice.business.persistence.repository.BookRepository;
import com.bookservice.business.web.dto.criteria.BookSearchCriteria;
import com.bookservice.business.web.dto.pagination.BookPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookCriteriaRepository criteriaRepository;

    public BookService(
        BookRepository bookRepository, BookCriteriaRepository criteriaRepository) {
        this.bookRepository = bookRepository;
        this.criteriaRepository = criteriaRepository;
    }

    public BookEntity findById(Long id) {
        return RestPreConditions.checkNotNull(bookRepository.findById(id), "Book with Id %s was not found", id);
    }

    public Page<BookEntity> findAll(
        BookPage page, BookSearchCriteria criteria){
        return criteriaRepository.findAllWithFilters(page, criteria);
    }

    public BookEntity insert(BookEntity entity) {
        log.info("Before save, checking if there is another book saved in the database with the same ISBN [{}]",
            entity.getIsbn());
        RestPreConditions.checkState(bookRepository.findByIsbn(entity.getIsbn()).isPresent(),
            "There is another book using the same isbn '%s' informed", entity.getIsbn());
        return bookRepository.save(entity);
    }

    public void update(BookEntity entity, Long id) {
        log.info("Before update, checking if the book exists...");
        if (!bookRepository.existsById(id)) {
            throw new DataNotFoundException("Book with Id %s was not found".formatted(id));
        }
        entity.setId(id);
        bookRepository.save(entity);
    }

    public void delete(Long id) {
        log.info("Before delete, checking if the book exists...");
        final var foundBook = findById(id);

        log.info("Planet found, deleting...");
        bookRepository.delete(foundBook);
    }
}
