package com.orderservice.business.web.helper;


import com.orderservice.business.persistence.entity.BookOrderEntity;
import com.orderservice.business.web.dto.BookOrderDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookOrderHelper {
    private final ModelMapper modelMapper;

    public BookOrderHelper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public BookOrderDTO toModel(BookOrderEntity entity) {
        return modelMapper.map(entity, BookOrderDTO.class);
    }
}
