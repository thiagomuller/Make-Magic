package com.thiagomuller.hpapi.exception;

public class CharacterNameAlreadyExistsException extends Exception{
	public CharacterNameAlreadyExistsException(String errorMessage) {
		super(errorMessage);
	}
}
