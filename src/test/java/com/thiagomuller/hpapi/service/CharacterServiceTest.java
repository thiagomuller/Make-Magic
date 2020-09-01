package com.thiagomuller.hpapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.thiagomuller.hpapi.CharacterProvider;
import com.thiagomuller.hpapi.HouseIdsProvider;
import com.thiagomuller.hpapi.exception.CharacterNameAlreadyExistsException;
import com.thiagomuller.hpapi.exception.CharacterNotFoundException;
import com.thiagomuller.hpapi.exception.InvalidHouseIdException;
import com.thiagomuller.hpapi.exception.NoCharactersFoundException;
import com.thiagomuller.hpapi.model.Character;
import com.thiagomuller.hpapi.repository.CharacterRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test", "unittest"})
public class CharacterServiceTest {
	
	@MockBean
	private HouseFinder houseFinder;
	
	@MockBean
	private CharacterRepository repository;
	
	@Autowired
	private CharacterService service;
	
	@Test
	@Tag("UnitTest")
	public void givenInvalidHouseIdWhenPostNewCharacterThenItShouldThrowInvalidHouseIdException() {
		Character hermione = CharacterProvider.getHermione();
		hermione.setHouseId("123");
		when(houseFinder.findAllHouses()).thenReturn(new ArrayList<String>());	
		Exception exception = assertThrows(InvalidHouseIdException.class, () -> {
			service.createOrUpdateCharacter(hermione);
		});
		verify(houseFinder, times(1)).findAllHouses();
		
		String expectedMessage = "Invalid house id";
	    String actualMessage = exception.getMessage();
	 
	    assertEquals(expectedMessage, actualMessage);
	}
	
	@Test
	@Tag("UnitTest")
	public void givenAValidHouseIdWhenPostNewCharacterThenItShouldCallRepository() 
			throws CharacterNameAlreadyExistsException{
		try {
			List<String> houseIds = HouseIdsProvider.getHouseIds();
			Character hermione = CharacterProvider.getHermione();
			when(houseFinder.findAllHouses()).thenReturn(houseIds);
			when(repository.save(hermione)).thenReturn(hermione);
			service.createOrUpdateCharacter(hermione);
			verify(repository, times(1)).save(hermione);
		}catch(InvalidHouseIdException invalidHouseEx) {
			invalidHouseEx.printStackTrace();
		}
	}
	
	@Test
	@Tag("UnitTest")
	public void givenANameThatAlreadyExistsWithNoIdWhenPostNewCharacterThenItShouldThrowCharacterNameAlreadyExists() 
			throws CharacterNameAlreadyExistsException, InvalidHouseIdException{
		Character hermione = CharacterProvider.getHermione();
		when(repository.findCharactersByName("Hermione Granger")).thenReturn(Optional.of(hermione));
		
		Exception exception = assertThrows(CharacterNameAlreadyExistsException.class , ()  ->{
			service.createOrUpdateCharacter(hermione);
		});
		String expectedMessage = "Character name already exists";
		String actualMessage = exception.getMessage();
		verify(repository, times(1)).findCharactersByName("Hermione Granger");
		assertEquals(expectedMessage, actualMessage);
		
	}
	
	@Test
	@Tag("UnitTest")
	public void givenSomeCharactersFromRepositoryWhenGetAllCharactersCalledThenItShouldReturnSameCharactersInList() {
		Character hermione = CharacterProvider.getHermione();
		Character rony = CharacterProvider.getRony();
		List<Character> characters = new ArrayList<>() {{
			add(hermione);
			add(rony);
		}};
		when(repository.findAll()).thenReturn(characters);
		List<Character> foundCharacters = service.getAllCharacters();
		for(int i = 0; i < characters.size(); i ++) {
			assertEquals(characters.get(i).getName(), foundCharacters.get(i).getName());
		}
		verify(repository, times(1)).findAll();
	}
	
	@Test
	@Tag("UnitTest")
	public void givenEmptyResponseFromRepositoryWhenGetAllCharactersCalledThenItShouldReturnEmptyList() {
		Iterable<Character> characters = new ArrayList<Character>();
		when(repository.findAll()).thenReturn(characters);
		List<Character> foundCharacters = service.getAllCharacters();
		assertEquals(0, foundCharacters.size());
	}
	
	@Test
	@Tag("UnitTest")
	public void givenDbWithSeveralCharactersWhenGetCharactersByHouseWithValidHouseIdThenShouldCallRepository() 
			throws NoCharactersFoundException, InvalidHouseIdException{
		Character hermione = CharacterProvider.getHermione();
		Character rony = CharacterProvider.getRony();
		Character harry = CharacterProvider.getHarry();
		List<Character> gryffyndorCharacters = new ArrayList<>();
		gryffyndorCharacters.add(harry);
		gryffyndorCharacters.add(hermione);
		gryffyndorCharacters.add(rony);
		List<String> validHouses = new ArrayList<>();
		validHouses.add("5a05e2b252f721a3cf2ea33f");
		when(houseFinder.findAllHouses()).thenReturn(validHouses);
		when(repository.findCharactersByHouseId("5a05e2b252f721a3cf2ea33f")).thenReturn(gryffyndorCharacters);
		service.getAllCharactersFromGivenHouse("5a05e2b252f721a3cf2ea33f");
		verify(repository, times(1)).findCharactersByHouseId("5a05e2b252f721a3cf2ea33f");
	}
	
	@Test
	@Tag("UnitTest")
	public void givenDbWithSeveralCharactersWhenGetCharactersByHouseWithInvalidHouseIdthenItShouldThrowInvalidHouseIdException() 
			throws InvalidHouseIdException{
		when(houseFinder.findAllHouses()).thenReturn(new ArrayList());
		Exception exception = assertThrows(InvalidHouseIdException.class, () -> {
			service.getAllCharactersFromGivenHouse("5a05e2b252f721a3cf2ea33f");
		});
		verify(houseFinder, times(1)).findAllHouses();
		
		String expectedMessage = "Invalid house id";
	    String actualMessage = exception.getMessage();
	    
		assertEquals(expectedMessage, actualMessage);
	}
	
	
	
	@Test
	@Tag("UnitTest")
	public void givenInvalidIdWhenDeleteteChracterByIdThenShouldThrowCharacterNotFoundException() {
		when(repository.findById(1)).thenReturn(Optional.empty());
		Exception exception = assertThrows(CharacterNotFoundException.class, () -> {
			service.deleteCharacterById(1);
		});
		
		verify(repository, times(1)).findById(1);
		String expectedMessage = "Character not found";
	    String actualMessage = exception.getMessage();
	 
	    assertEquals(expectedMessage, actualMessage);
	}
	
	@Test
	@Tag("UnitTest")
	public void givenValidIdWhennDeleteteChracterByIdThenShouldCallRepository() {
		try {
			Character hermione = CharacterProvider.getHermione();
			when(repository.findById(1)).thenReturn(Optional.of(hermione));
			service.deleteCharacterById(1);
			verify(repository, times(1)).delete(hermione);
		}catch (CharacterNotFoundException characterNotFoundEx) {
			characterNotFoundEx.printStackTrace();
		}
	}
	
	@Test
	@Tag("UnitTest")
	public void givenNoCharactersInDbWhenDeleteAllThenShouldThrowNoCharactersFoundException() {
		when(repository.findAll()).thenReturn(new ArrayList<Character>());
		Exception exception = assertThrows(NoCharactersFoundException.class, () -> {
			service.deleteAllCharacters();
		});
		
		verify(repository, times(1)).findAll();
		String expectedMessage = "No characters found";
	    String actualMessage = exception.getMessage();
	 
	    assertEquals(expectedMessage, actualMessage);
		
	}
	
	@Test
	@Tag("UnitTest")
	public void givenDbHasCharactersWhenDeleteAllThenShouldCallRepository()
			throws NoCharactersFoundException{
		Character hermione = CharacterProvider.getHermione();
		List<Character> characters = new ArrayList<>();
		characters.add(hermione);
		when(repository.findAll()).thenReturn(characters);
		service.deleteAllCharacters();
		verify(repository, times(1)).deleteAll();
	}
}
