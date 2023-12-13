package org.thiagocircuncisao.configuration;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.thiagocircuncisao.exception.TransactionException;
import org.thiagocircuncisao.presentation.MessageResponse;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class, TransactionException.class
	})
	protected ResponseEntity<Object> handleGeneric(RuntimeException ex, WebRequest request,
			HttpServletRequest httpRequest) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new MessageResponse(ex.getLocalizedMessage(), httpRequest.getRequestURI()));
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		String URI = ((ServletWebRequest) request).getRequest().getRequestURI().toString();
		StringBuilder errorsSB = new StringBuilder();
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
			errorsSB.append(error.getDefaultMessage() + "; ");
		});
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse(errorsSB.toString(), URI));
	}
}