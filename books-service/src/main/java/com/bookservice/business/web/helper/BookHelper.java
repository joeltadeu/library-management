package com.bookservice.business.web.helper;

import com.bookservice.business.web.dto.BookInsertUpdateDTO;
import com.bookservice.business.persistence.entity.AuthorEntity;
import com.bookservice.business.persistence.entity.BookEntity;
import com.bookservice.business.persistence.entity.CategoryEntity;
import com.bookservice.business.web.dto.BookDTO;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
public class BookHelper {
    private final ModelMapper modelMapper;

    public BookHelper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public BookEntity toEntity(BookDTO book){
        return modelMapper.map(book, BookEntity.class);
    }

    public BookEntity toEntity(BookInsertUpdateDTO book){
        return BookEntity.builder()
            .isbn(book.getIsbn())
            .title(book.getTitle())
            .categories(book.getCategories().stream().map(i -> new CategoryEntity(i, null)).collect(Collectors.toSet()))
            .author(AuthorEntity.builder().id(book.getAuthorId()).build())
            .build();
    }

    public BookDTO toModel(BookEntity book){
        return modelMapper.map(book, BookDTO.class);
    }

    public List<BookDTO> toModel(List<BookEntity> books){
        return books.stream()
            .map(this::toModel)
            .collect(toList());
    }

    public Page<BookDTO> toModel(Page<BookEntity> bookPage) {
        List<BookDTO> books = bookPage.stream()
            .map(this::toModel)
            .collect(toList());
        return new PageImpl<>(books, bookPage.getPageable(), bookPage.getTotalElements());
    }
}
