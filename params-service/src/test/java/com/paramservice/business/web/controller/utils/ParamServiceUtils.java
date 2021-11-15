package com.paramservice.business.web.controller.utils;

import com.paramservice.business.persistence.entity.ParamEntity;
import static com.paramservice.business.web.controller.utils.TestUtils.getRandomLong;

public class ParamServiceUtils {

    public static ParamEntity getParamEntity(String key) {
        return ParamEntity.builder()
            .id(getRandomLong())
            .key(key)
            .value(3)
            .build();
    }

}
