package com.bookservice.commons.exception.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {

  private Integer code;
  private String status;
  private String description;
  private String date;

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private List<AttributeMessage> attributes;

  public ExceptionResponse(HttpStatus httpStatus, String description, List<AttributeMessage> attributes) {
    this.code = httpStatus.value();
    this.status = httpStatus.getReasonPhrase();
    this.description = description;
    this.date = LocalDateTime.now().toString();
    this.attributes = attributes;
  }

  public ExceptionResponse(HttpStatus httpStatus, String description) {
    this.code = httpStatus.value();
    this.status = httpStatus.getReasonPhrase();
    this.description = description;
    this.date = LocalDateTime.now().toString();
    this.attributes = new ArrayList<>();
  }

  public ExceptionResponse(HttpStatus httpStatus) {
    this.code = httpStatus.value();
    this.status = httpStatus.getReasonPhrase();
    this.date = LocalDateTime.now().toString();
    this.attributes = new ArrayList<>();
  }
}
