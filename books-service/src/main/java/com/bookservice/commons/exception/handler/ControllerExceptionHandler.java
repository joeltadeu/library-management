package com.bookservice.commons.exception.handler;

import com.bookservice.commons.exception.DataNotFoundException;
import com.bookservice.commons.exception.BadRequestException;
import com.bookservice.commons.exception.model.AttributeMessage;
import com.bookservice.commons.exception.model.ExceptionResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.ValidationException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ControllerExceptionHandler {

	private static final String VALIDATION_EXCEPTION_MSG = "Validation Exception";

	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> constraintViolationException(ConstraintViolationException e) {
		List<AttributeMessage> attributeMessages = new ArrayList<>();

		String field = "";
		for (ConstraintViolation violation : e.getConstraintViolations()) {
			for (Path.Node node : violation.getPropertyPath()) {
				field = node.getName();
			}
			attributeMessages.add(new AttributeMessage(field, violation.getMessage()));
		}

		ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, VALIDATION_EXCEPTION_MSG, attributeMessages);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(err);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> dataIntegrityViolationException(DataIntegrityViolationException e) {
		ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(err);
	}

	@ExceptionHandler(AccessDeniedException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> accessDeniedException(AccessDeniedException e) {
		ExceptionResponse err = new ExceptionResponse(HttpStatus.UNAUTHORIZED, e.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body(err);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
		ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, VALIDATION_EXCEPTION_MSG);
		Map<String, AttributeMessage> errors = new HashMap<>();
		for (FieldError x : e.getBindingResult()
				.getFieldErrors()) {
			if (errors.containsKey(x.getField())) {
				AttributeMessage attributeMessage = errors.get((x.getField()));
				attributeMessage.addError(x.getDefaultMessage());
			} else {
				errors.put(x.getField(), new AttributeMessage(x.getField(), x.getDefaultMessage()));
			}
		}

		err.getAttributes()
				.addAll(errors.values());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(err);
	}

	@ExceptionHandler(DataNotFoundException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> dataNotFoundException(DataNotFoundException e) {
		ExceptionResponse err = new ExceptionResponse(HttpStatus.NOT_FOUND, e.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body(err);
	}

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> exception(Exception e) {
		ExceptionResponse err = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(err);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> missingServletRequestParameter(MissingServletRequestParameterException ex) {
		ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(err);
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> illegalArgumentException(IllegalArgumentException e) {
		ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(err);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> httpMessageNotReadableException(HttpMessageNotReadableException e) {
		ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(err);
	}

	@ExceptionHandler(BadRequestException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> handlingBadRequestException(BadRequestException badRequestException) {
		ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, badRequestException.getMessage(),
				badRequestException.getMessages());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(err);
	}

	@ExceptionHandler(ValidationException.class)
	@ResponseBody
	public ResponseEntity<ExceptionResponse> handlingValidationException(ValidationException validationException) {
		ExceptionResponse err = new ExceptionResponse(HttpStatus.BAD_REQUEST, validationException.getMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(err);
	}
}

