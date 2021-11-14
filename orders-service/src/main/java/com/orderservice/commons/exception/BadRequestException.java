package com.orderservice.commons.exception;

import com.orderservice.commons.exception.model.AttributeMessage;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@ToString
public class BadRequestException extends RuntimeException {
  private static final long serialVersionUID = 136827568356021732L;

  private HttpStatus status;

  private List<AttributeMessage> messages;
  private String message;

  public BadRequestException(List<AttributeMessage> messages) {
    this.status = HttpStatus.BAD_REQUEST;
    this.messages = messages;
  }

  public BadRequestException(String message) {
    this.status = HttpStatus.BAD_REQUEST;
    this.message = message;
  }

}
