package com.paramservice.business.web.helper;

import com.paramservice.business.persistence.entity.ParamEntity;
import com.paramservice.business.web.dto.ParamDTO;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ParamHelper {
    private final ModelMapper modelMapper;

    public ParamHelper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ParamDTO toModel(ParamEntity param){
        return modelMapper.map(param, ParamDTO.class);
    }
}
