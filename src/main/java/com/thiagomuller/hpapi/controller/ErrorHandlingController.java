package com.thiagomuller.hpapi.controller;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.thiagomuller.hpapi.exception.CharacterNameAlreadyExistsException;
import com.thiagomuller.hpapi.exception.CharacterNotFoundException;
import com.thiagomuller.hpapi.exception.InvalidHouseIdException;
import com.thiagomuller.hpapi.exception.NoCharactersFoundException;
import com.thiagomuller.hpapi.exception.ValidationErrorResponse;
import com.thiagomuller.hpapi.exception.Violation;

@ControllerAdvice
public class ErrorHandlingController {
	

	  @ExceptionHandler(ConstraintViolationException.class)
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ResponseBody
	  public ValidationErrorResponse onConstraintValidationException(ConstraintViolationException e) {
	    ValidationErrorResponse error = new ValidationErrorResponse();
	    for (ConstraintViolation violation : e.getConstraintViolations()) {
	      error.getViolations().add(
	        new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
	    }
	    return error;
	  }
	  
	  @ExceptionHandler(CharacterNameAlreadyExistsException.class)
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ResponseBody
	  public ResponseEntity onCharacterNameAlreadyExistsException(CharacterNameAlreadyExistsException characterNameAlreadyExists) {
		  return new ResponseEntity(characterNameAlreadyExists.getMessage(), HttpStatus.FORBIDDEN);
	  }
	  
	  @ExceptionHandler(InvalidHouseIdException.class)
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ResponseBody
	  public ResponseEntity onInvalidHouseIdException(InvalidHouseIdException houseNotFoundEx) {
		  return new ResponseEntity(houseNotFoundEx.getMessage(), HttpStatus.BAD_REQUEST);
	  }
	  
	  @ExceptionHandler(CharacterNotFoundException.class)
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ResponseBody
	  public ResponseEntity onCharacterNotFoundException(CharacterNotFoundException characterNotFoundEx){
		  return new ResponseEntity(characterNotFoundEx.getMessage(), HttpStatus.BAD_REQUEST);
	  }
	  
	  @ExceptionHandler(NoCharactersFoundException.class)
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ResponseBody
	  public ResponseEntity onNoCharactersFoundException(NoCharactersFoundException noCharactersEx){
		  return new ResponseEntity(noCharactersEx.getMessage(), HttpStatus.NOT_FOUND);
	  }
}