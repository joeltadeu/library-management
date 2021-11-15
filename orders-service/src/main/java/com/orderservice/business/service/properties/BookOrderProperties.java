package com.orderservice.business.service.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookOrderProperties {
    @Value("${lmgn.params.limit}")
    private String borrowLimitKey;

    @Value("${lmgn.params.days}")
    private String borrowDaysKey;
}
