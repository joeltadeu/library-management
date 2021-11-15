package com.paramservice.business.service;

import com.paramservice.business.persistence.repository.ParamRepository;
import com.paramservice.commons.exception.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.paramservice.business.web.controller.utils.ParamServiceUtils.getParamEntity;
import static com.paramservice.business.web.controller.utils.TestUtils.getRandomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ParamServiceTest {

    @InjectMocks
    private ParamService paramService;

    @Mock
    private ParamRepository paramRepositoryMock;

    @Test
    public void findParamById_shouldFind() {
        final var paramKey = getRandomString();
        final var returnedParam = getParamEntity(paramKey);

        when(paramRepositoryMock.findByKey(paramKey)).thenReturn(Optional.of(returnedParam));

        final var paramReturnedFromDatabase = paramService.findByKey(paramKey);

        assertEquals(returnedParam, paramReturnedFromDatabase);

        verify(paramRepositoryMock, times(1)).findByKey(paramKey);
    }

    @Test
    public void findParamById_shouldNotFind() {
        final var paramKey = getRandomString();

        when(paramRepositoryMock.findByKey(paramKey)).thenReturn(Optional.empty());

        final var assertThrows = assertThrows(DataNotFoundException.class, () -> paramService.findByKey(paramKey));

        assertEquals("Key %s was not found".formatted(paramKey), assertThrows.getMessage());

        verify(paramRepositoryMock, times(1)).findByKey(paramKey);
    }
}
