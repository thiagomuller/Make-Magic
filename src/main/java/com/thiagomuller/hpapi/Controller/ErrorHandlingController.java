package com.thiagomuller.hpapi.Controller;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.thiagomuller.hpapi.Exception.CharacterNotFoundException;
import com.thiagomuller.hpapi.Exception.InvalidHouseIdException;
import com.thiagomuller.hpapi.Exception.NoCharactersFoundException;
import com.thiagomuller.hpapi.Exception.NoHousesFoundException;
import com.thiagomuller.hpapi.Exception.ValidationErrorResponse;
import com.thiagomuller.hpapi.Exception.Violation;

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

	  
	  @ExceptionHandler(InvalidHouseIdException.class)
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ResponseBody
	  public ResponseEntity onInvalidHouseIdException(InvalidHouseIdException houseNotFoundEx) {
		  return new ResponseEntity(houseNotFoundEx.getMessage(), HttpStatus.BAD_REQUEST);
	  }
	  
	  @ExceptionHandler(NoHousesFoundException.class)
	  @ResponseStatus(HttpStatus.BAD_REQUEST)
	  @ResponseBody
	  public ResponseEntity onNoHousesFoundException(NoHousesFoundException noHousesFoundEx) {
		  return new ResponseEntity(noHousesFoundEx.getMessage(), HttpStatus.NOT_FOUND);
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