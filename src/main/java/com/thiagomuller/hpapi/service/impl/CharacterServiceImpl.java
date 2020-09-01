package com.thiagomuller.hpapi.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.thiagomuller.hpapi.exception.CharacterNameAlreadyExistsException;
import com.thiagomuller.hpapi.exception.CharacterNotFoundException;
import com.thiagomuller.hpapi.exception.InvalidHouseIdException;
import com.thiagomuller.hpapi.exception.NoCharactersFoundException;
import com.thiagomuller.hpapi.model.Character;
import com.thiagomuller.hpapi.repository.CharacterRepository;
import com.thiagomuller.hpapi.service.CharacterService;
import com.thiagomuller.hpapi.service.HouseFinder;

import lombok.extern.java.Log;

@Service
@Log
public class CharacterServiceImpl implements CharacterService{
	
	@Autowired
	private CharacterRepository repository;
	
	@Autowired
	private HouseFinder houseFinder;

	@Override
	public Character createOrUpdateCharacter(Character character) throws InvalidHouseIdException, 
	CharacterNameAlreadyExistsException{
		if(character.getCharacterId() ==  null)
			validateCharacterName(character.getName());
		validateHouseId(character.getHouseId());
		return repository.save(character);
	}

	@Cacheable("characters")
	@Override
	public List<Character> getAllCharacters(){
		Iterable<Character> foundCharacters = repository.findAll();
		if(getIterableSize(foundCharacters) == 0)
			return new ArrayList<Character>();
		List<Character> characters = new ArrayList<>();
		for(Character character : foundCharacters)
			characters.add(character);
		return characters;
	}
	
	@Override
	public Optional<Character> getCharacterById(Integer characterId){
		return repository.findById(characterId);
	}

	@Override
	public List<Character> getAllCharactersFromGivenHouse(String houseId) throws InvalidHouseIdException{
		validateHouseId(houseId);
		List<Character> charactersForGivenHouse = repository.findCharactersByHouseId(houseId);
		if(charactersForGivenHouse.isEmpty())
			return new ArrayList<Character>();
		return charactersForGivenHouse;
	}

	@Override
	public void deleteCharacterById(Integer characterId) throws CharacterNotFoundException {
		Optional<Character> character = repository.findById(characterId);
		if(character.isEmpty())
			throw new CharacterNotFoundException(String.format("Character not found", 
					characterId));
		repository.delete(character.get());
	}

	@Override
	public void deleteAllCharacters() throws NoCharactersFoundException {
		Iterable<Character> foundCharacters = repository.findAll();
		if(getIterableSize(foundCharacters) == 0)
			throw new NoCharactersFoundException("No characters found");
		List<Character> characters = new ArrayList<>();
		for(Character character : foundCharacters) {
			characters.add(character);
		}
		repository.deleteAll();
	}
	
	private void validateHouseId(String houseId) throws InvalidHouseIdException{
		List<String> potterApiHouseIds = getAllHouses();
		if(!potterApiHouseIds.contains(houseId))
			throw new InvalidHouseIdException("Invalid house id");
	}
	
	public List<String> getAllHouses(){
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
	
	private void validateCharacterName(String name) throws CharacterNameAlreadyExistsException{
		Optional<Character> character = repository.findCharactersByName(name);
		if(!character.isEmpty())
			throw new CharacterNameAlreadyExistsException("Character name already exists");
	}
}
