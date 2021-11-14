package com.paramservice.business.service;

import com.paramservice.business.persistence.entity.BookEntity;
import com.paramservice.business.persistence.repository.BookCriteriaRepository;
import com.paramservice.business.persistence.repository.BookRepository;
import com.paramservice.business.web.dto.criteria.BookSearchCriteria;
import com.paramservice.business.web.dto.pagination.BookPage;
import com.paramservice.commons.validation.RestPreConditions;
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
            "There is another book using the same isbn '%s' informed ", entity.getIsbn());
        return bookRepository.save(entity);
    }

    public void update(BookEntity entity, Long id) {
        entity.setId(id);
        bookRepository.save(entity);
    }

    public void delete(Long id){
        bookRepository.deleteById(id);
    }
}
