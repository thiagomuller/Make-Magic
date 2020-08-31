package com.thiagomuller.hpapi.Service.Impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.thiagomuller.hpapi.Exception.CharacterNotFoundException;
import com.thiagomuller.hpapi.Exception.InvalidHouseIdException;
import com.thiagomuller.hpapi.Exception.NoCharactersFoundException;
import com.thiagomuller.hpapi.Exception.NoHousesFoundException;
import com.thiagomuller.hpapi.Model.Character;
import com.thiagomuller.hpapi.Repository.CharacterRepository;
import com.thiagomuller.hpapi.Service.CharacterService;
import com.thiagomuller.hpapi.Service.HouseFinder;

@Service
public class CharacterServiceImpl implements CharacterService{
	
	@Autowired
	private CharacterRepository characterRepository;
	
	@Autowired
	private HouseFinder houseFinder;

	@Override
	public Character createOrUpdateCharacter(Character character) throws InvalidHouseIdException, NoHousesFoundException{
		validateHouseId(character.getHouseId());
		return characterRepository.save(character);
	}

	@Override
	public List<Character> getAllCharacters() throws NoCharactersFoundException{
		Iterable<Character> foundCharacters = characterRepository.findAll();
		if(getIterableSize(foundCharacters) == 0)
			throw new NoCharactersFoundException("No characters found");
		List<Character> characters = new ArrayList<>();
		for(Character character : foundCharacters)
			characters.add(character);
		return characters;
	}
	
	@Override
	public Character getCharacterById(Integer characterId) throws CharacterNotFoundException {
		Optional<Character> foundCharacter = characterRepository.findById(characterId);
		if(foundCharacter.isEmpty())
			throw new CharacterNotFoundException(String.format("No character found with id %d", characterId));
		return foundCharacter.get();
	}

	@Override
	public List<Character> getAllCharactersFromGivenHouse(String houseId) throws InvalidHouseIdException, NoHousesFoundException, NoCharactersFoundException{
		validateHouseId(houseId);
		List<Character> charactersForGivenHouse = characterRepository.findCharactersByHouseId(houseId);
		if(charactersForGivenHouse.isEmpty())
			throw new NoCharactersFoundException(String.format("No characters found for house id %s", houseId));
		return charactersForGivenHouse;
	}

	@Override
	public void deleteCharacterById(Integer characterId) throws CharacterNotFoundException {
		Optional<Character> character = characterRepository.findById(characterId);
		if(character.isEmpty())
			throw new CharacterNotFoundException(String.format("Character with id %d not found", 
					characterId));
		characterRepository.delete(character.get());
	}

	@Override
	public void deleteAllCharacters() throws NoCharactersFoundException {
		Iterable<Character> foundCharacters = characterRepository.findAll();
		if(getIterableSize(foundCharacters) == 0)
			throw new NoCharactersFoundException("No characters were found");
		List<Character> characters = new ArrayList<>();
		for(Character character : foundCharacters) {
			characters.add(character);
		}
		characterRepository.deleteAll();
	}
	
	private void validateHouseId(String houseId) throws NoHousesFoundException, InvalidHouseIdException{
		List<String> potterApiHouseIds = getAllHouses();
		if(!potterApiHouseIds.contains(houseId))
			throw new InvalidHouseIdException("Invalid house id");
	}
	
	@Cacheable("housesCache")
	private List<String> getAllHouses() throws NoHousesFoundException{
		return houseFinder.findAllHouses();
	}
	
	private Integer getIterableSize(Iterable data) {
		if (data instanceof Collection) {
	        return ((Collection<?>) data).size();
	    }
	    int counter = 0;
	    for (Object i : data) {
	        counter++;
	    }
	    return counter;
	}
}
