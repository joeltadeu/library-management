package com.bookservice.infrastructure.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ModelMapper nonNullMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
            .setCollectionsMergeEnabled(true)
            .setDeepCopyEnabled(true)
            .setSkipNullEnabled(true)
            .setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
}
