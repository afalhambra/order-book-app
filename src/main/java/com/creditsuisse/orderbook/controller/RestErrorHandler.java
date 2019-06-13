package com.creditsuisse.orderbook.controller;

import java.util.Optional;

import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

import io.undertow.util.BadRequestException;

/**
 * Rest handle controller to handle non-business exceptions 
 * @author afernandeza
 *
 */
@Controller
@ControllerAdvice
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
public class RestErrorHandler {

	private ResponseEntity<VndErrors> error(final Exception exception, final HttpStatus httpStatus,
			final String logRef) {
		final String message = Optional.of(exception.getMessage()).orElse(exception.getClass().getSimpleName());
		return new ResponseEntity<>(new VndErrors(logRef, message), httpStatus);
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<VndErrors> handleAll(final Exception e) {
		return error(e, HttpStatus.INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
	}

	@ExceptionHandler({ NoHandlerFoundException.class })
	public ResponseEntity<VndErrors> handleNotFoundError(final NoHandlerFoundException e) {
		return error(e, HttpStatus.NOT_FOUND, e.getLocalizedMessage());
	}

	@ExceptionHandler({ BadRequestException.class })
	public ResponseEntity<VndErrors> handleBadRequestError(final BadRequestException e) {
		return error(e, HttpStatus.BAD_REQUEST, e.getLocalizedMessage());
	}

	@ExceptionHandler({ HttpRequestMethodNotSupportedException.class })
	public ResponseEntity<VndErrors> handleHttpRequestMethodNotSupported(
			final HttpRequestMethodNotSupportedException e) {
		return error(e, HttpStatus.METHOD_NOT_ALLOWED, e.getLocalizedMessage());
	}

	@ExceptionHandler({ HttpMediaTypeNotSupportedException.class })
	public ResponseEntity<VndErrors> handleHttpMediaTypeNotSupported(final HttpMediaTypeNotSupportedException e) {
		return error(e, HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getLocalizedMessage());
	}

}