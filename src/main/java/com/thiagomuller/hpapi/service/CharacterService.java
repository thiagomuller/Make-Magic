package com.thiagomuller.hpapi.service;

import java.util.List;
import java.util.Optional;

import com.thiagomuller.hpapi.exception.CharacterNameAlreadyExistsException;
import com.thiagomuller.hpapi.exception.CharacterNotFoundException;
import com.thiagomuller.hpapi.exception.InvalidHouseIdException;
import com.thiagomuller.hpapi.exception.NoCharactersFoundException;
import com.thiagomuller.hpapi.model.Character;

public interface CharacterService {
	public Character createOrUpdateCharacter(Character character) throws InvalidHouseIdException, CharacterNameAlreadyExistsException;
	public Optional<Character> getCharacterById(Integer characterId);
	public List<Character> getAllCharacters();
	public List<Character> getAllCharactersFromGivenHouse(String houseId) throws InvalidHouseIdException;
	public void deleteCharacterById(Integer characterId) throws CharacterNotFoundException;
	public void deleteAllCharacters() throws NoCharactersFoundException;
}
