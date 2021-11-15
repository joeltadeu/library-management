package com.orderservice.business.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class BookOrderDTO {
    @Schema(description = "Loan Id",
            name = "id",
            example = "19")
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Schema(description = "Date the book was borrowed",
            name = "loanedAt",
            example = "2021-10-20 14:55:00")
    private LocalDateTime loanedAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Schema(description = "Date the book must be returned",
            name = "returnIn",
            example = "2021-10-25 14:55:00")
    private LocalDateTime returnIn;

    @JsonInclude(Include.NON_NULL)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @Schema(description = "Date the book was returned",
            name = "returnedAt",
            example = "2021-10-25 14:55:00")
    private LocalDateTime returnedAt;
}
