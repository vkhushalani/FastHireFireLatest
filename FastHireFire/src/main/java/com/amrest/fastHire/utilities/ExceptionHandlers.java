package com.amrest.fastHire.utilities;

/*
 * Utilities class <h1>ExceptionHandlers</h1>
 * 
 * @author : Aseem Wangoo
 * @version : 2.0
 */
import javax.naming.AuthenticationException;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.sap.core.connectivity.api.configuration.NotAuthorizedException;

@RestControllerAdvice
public class ExceptionHandlers {

	@ExceptionHandler(value = { NotAuthorizedException.class })
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public ErrorManager unauthorisedException(AuthenticationException ex) {
		return new ErrorManager(401, 4010, ex.getMessage());
	}

	@ExceptionHandler(value = { NoHandlerFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public ErrorManager noHandlerFoundException(Exception ex) {
		return new ErrorManager(404, 4041, ex.getMessage());
	}

	@ExceptionHandler(value = { ConstraintViolationException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ErrorManager constraintViolationException(ConstraintViolationException ex) {
		return new ErrorManager(500, 5001, ex.getMessage());
	}

	@ExceptionHandler(value = { Exception.class })
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ErrorManager unknownException(Exception ex) {
		return new ErrorManager(500, 5002, ex.getMessage());
	}

}
