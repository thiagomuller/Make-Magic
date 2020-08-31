package com.thiagomuller.hpapi.Service;

import java.util.List;

import com.thiagomuller.hpapi.Exception.CharacterNotFoundException;
import com.thiagomuller.hpapi.Exception.InvalidHouseIdException;
import com.thiagomuller.hpapi.Exception.NoCharactersFoundException;
import com.thiagomuller.hpapi.Exception.NoHousesFoundException;
import com.thiagomuller.hpapi.Model.Character;

public interface CharacterService {
	public Character createOrUpdateCharacter(Character character) throws InvalidHouseIdException, NoHousesFoundException;
	public Character getCharacterById(Integer characterId) throws CharacterNotFoundException;
	public List<Character> getAllCharacters() throws NoCharactersFoundException;
	public List<Character> getAllCharactersFromGivenHouse(String houseId) throws InvalidHouseIdException, NoHousesFoundException, NoCharactersFoundException;
	public void deleteCharacterById(Integer characterId) throws CharacterNotFoundException;
	public void deleteAllCharacters() throws NoCharactersFoundException;
}
