package com.thiagomuller.hpapi.Service;

import java.util.List;
import java.util.Optional;

import com.thiagomuller.hpapi.Exception.CharacterNotFoundException;
import com.thiagomuller.hpapi.Exception.InvalidHouseIdException;
import com.thiagomuller.hpapi.Exception.NoCharactersFoundException;
import com.thiagomuller.hpapi.Model.Character;

public interface CharacterService {
	public Character createOrUpdateCharacter(Character character) throws InvalidHouseIdException;
	public Optional<Character> getCharacterById(Integer characterId);
	public List<Character> getAllCharacters();
	public List<Character> getAllCharactersFromGivenHouse(String houseId) throws InvalidHouseIdException;
	public void deleteCharacterById(Integer characterId) throws CharacterNotFoundException;
	public void deleteAllCharacters() throws NoCharactersFoundException;
}
