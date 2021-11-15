package com.paramservice.business.web.controller;

import com.paramservice.Application;
import com.paramservice.business.persistence.entity.ParamEntity;
import com.paramservice.business.service.ParamService;
import com.paramservice.business.web.helper.ParamHelper;
import com.paramservice.commons.exception.DataNotFoundException;
import com.paramservice.infrastructure.config.ModelMapperConfig;
import com.paramservice.infrastructure.config.SleuthConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static com.paramservice.business.web.controller.utils.TestUtils.getRandomInteger;
import static com.paramservice.business.web.controller.utils.TestUtils.getRandomLong;
import static com.paramservice.business.web.controller.utils.TestUtils.getRandomString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest({ParamController.class})
@ContextConfiguration(classes = { Application.class, SleuthConfig.class, ModelMapperConfig.class, ParamHelper.class })
public class ParamControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ParamService paramServiceMock;

    @Test
    public void findParamByKey_shouldFind() throws Exception {
        final var paramId = getRandomLong();
        final var paramKey = getRandomString();
        final var paramValue = getRandomInteger();
        final var param = new ParamEntity(paramId, paramKey, paramValue);

        when(paramServiceMock.findByKey(paramKey)).thenReturn(param);

        mockMvc.perform(get("/api/v1/params/by-key/{key}", paramKey).contentType(APPLICATION_JSON))
            .andExpect(status().is(200))
            .andExpect(jsonPath("key").value(paramKey))
            .andExpect(jsonPath("value").value(paramValue));

        verify(paramServiceMock, times(1)).findByKey(paramKey);
    }

    @Test
    public void findBookById_shouldThrowNotFoundWhenNotFound() throws Exception {
        final var paramKey = getRandomString();
        when(paramServiceMock.findByKey(paramKey)).thenThrow(
            new DataNotFoundException("Key %s was not found".formatted(paramKey)));

        mockMvc.perform(get("/api/v1/params/by-key/{key}", paramKey).contentType(APPLICATION_JSON))
            .andExpect(status().is(404))
            .andExpect(jsonPath("description").value("Key %s was not found".formatted(paramKey)));

        verify(paramServiceMock, times(1)).findByKey(paramKey);
    }
}
