package com.paramservice.commons.exception.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AttributeMessage implements Serializable {
  private static final long serialVersionUID = 1L;

  private String attribute;
  private List<String> errors = new ArrayList<>();

  public AttributeMessage(String attribute, String error) {
    this.attribute = attribute;
    addError(error);
  }

  public void addError(String error) {
    this.errors.add(error);
  }
}
