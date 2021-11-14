package com.paramservice.business.service;

import com.paramservice.business.persistence.entity.ParamEntity;
import com.paramservice.business.persistence.repository.ParamRepository;
import com.paramservice.commons.validation.RestPreConditions;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ParamService {

    private final ParamRepository repository;

    public ParamService(ParamRepository repository) {
        this.repository = repository;
    }

    public ParamEntity findByKey(String key) {
        log.info("Finding param by key...");
        return RestPreConditions.checkNotNull(repository.findByKey(key), "Key %s was not found", key);
    }
}
