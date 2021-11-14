package com.paramservice.commons.exception;

import java.io.Serializable;
import java.util.function.Supplier;

public class DataNotFoundException extends RuntimeException {

  public DataNotFoundException(String message) {
    super(message);
  }

  public static <T extends Serializable> Supplier<DataNotFoundException> notFound(final T entityId) {
    return () -> new DataNotFoundException("Could not find data identified by " + entityId);
  }
}