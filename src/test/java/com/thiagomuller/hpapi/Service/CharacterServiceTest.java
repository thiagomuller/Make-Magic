package com.thiagomuller.hpapi.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.thiagomuller.hpapi.Exception.CharacterNotFoundException;
import com.thiagomuller.hpapi.Exception.InvalidHouseIdException;
import com.thiagomuller.hpapi.Exception.NoCharactersFoundException;
import com.thiagomuller.hpapi.Exception.NoHousesFoundException;
import com.thiagomuller.hpapi.Model.Character;
import com.thiagomuller.hpapi.Repository.CharacterRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test", "unittest"})
public class CharacterServiceTest {
	
	@MockBean
	private HouseFinder houseFinder;
	
	@MockBean
	private CharacterRepository characterRepository;
	
	@Autowired
	private CharacterService characterService;
	
	private static Character hermione;
	private static Character severus;
	private static Character rony;
	private static Character harry;
	
	private static List<String> houseIds;
	
	@BeforeAll
	public static void setup() {
		hermione = new Character();
		houseIds = new ArrayList<>();
		hermione.setName("Hermione Granger");
		hermione.setSchool("Hogwarts School of Witchcraft and Wizardry");
		hermione.setHouseId("5a05e2b252f721a3cf2ea33f");
		hermione.setPatronus("Otter");
		hermione.setRole("Student");
		hermione.setBloodStatus("Muggle-Born");
		houseIds.add("5a05e2b252f721a3cf2ea33f");
		
		severus = new Character();
		severus.setName("Severus Snape");
		severus.setRole("Professor");
		severus.setPatronus("Doe");
		severus.setSchool("Hogwarts School of Witchcraft and Wizardry");
		severus.setHouseId("5a05dc8cd45bd0a11bd5e071");
		severus.setBloodStatus("Pure-Blood");
		
		rony = new Character();
		rony.setName("Rony Weasley");
		rony.setSchool("Hogwarts School of Witchcraft and Wizardry");
		rony.setPatronus("Rat");
		rony.setRole("Student");
		rony.setHouseId("5a05e2b252f721a3cf2ea33f");
		rony.setBloodStatus("Pure-Blood");
		
		harry = new Character();
		harry.setName("Harry Potter");
		harry.setRole("Student");
		harry.setSchool("Hogwarts School of Witchcraft and Wizardry");
		harry.setHouseId("5a05e2b252f721a3cf2ea33f");
		harry.setPatronus("Deer");
		harry.setBloodStatus("Pure-Blood");
		
	}
	
	@Test
	@Tag("UnitTest")
	public void givenInvalidHouseIdWhenPostNewCharacterThenItShouldThrowInvalidHouseIdException() {
		try {
			hermione.setHouseId("123");
			when(houseFinder.findAllHouses()).thenReturn(new ArrayList<String>());	
			Exception exception = assertThrows(InvalidHouseIdException.class, () -> {
				characterService.createOrUpdateCharacter(hermione);
			});
			verify(houseFinder, times(1)).findAllHouses();
			
			String expectedMessage = "Invalid house id";
		    String actualMessage = exception.getMessage();
		 
		    assertEquals(expectedMessage, actualMessage);
		}catch (NoHousesFoundException noHousesEx) {
			noHousesEx.printStackTrace();
		}finally {
			hermione.setHouseId("5a05e2b252f721a3cf2ea33f");
		}
	}
	
	@Test
	@Tag("UnitTest")
	public void givenAValidHouseIdWhenPostNewCharacterThenItShouldCallRepository() {
		try {
			when(houseFinder.findAllHouses()).thenReturn(houseIds);
			when(characterRepository.save(hermione)).thenReturn(hermione);
			characterService.createOrUpdateCharacter(hermione);
			verify(characterRepository, times(1)).save(hermione);
		}catch (NoHousesFoundException noHousesEx) {
			noHousesEx.printStackTrace();
		}catch(InvalidHouseIdException invalidHouseEx) {
			invalidHouseEx.printStackTrace();
		}
	}
	
	@Test
	@Tag("UnitTest")
	public void givenValidIdWhenGetCharacterByIdThenItShouldReturnCharacter() throws CharacterNotFoundException{
		when(characterRepository.findById(1)).thenReturn(Optional.of(hermione));
		Character foundCharacter = characterService.getCharacterById(1);
		verify(characterRepository, times(1)).findById(1);
		assertEquals(hermione, foundCharacter);
		
	}
	
	@Test
	@Tag("UnitTest")
	public void givenInvalidIdWhenGetCharacterByIdThenIShouldThrowCharacterNotFoundException() {
		when(characterRepository.findById(1)).thenReturn(Optional.empty());
		Exception exception = assertThrows(CharacterNotFoundException.class, () -> {
			characterService.getCharacterById(1);
		});
		
		verify(characterRepository, times(1)).findById(1);
		String expectedMessage = "No character found with id 1";
		String actualMessage = exception.getMessage();
		
		assertEquals(expectedMessage, actualMessage);
	}
	
	@Test
	@Tag("UnitTest")
	public void givenDbWithSeveralCharactersWhenGetCharactersByHouseWithValidHouseIdThenShouldReturnCharacters() 
			throws NoCharactersFoundException, NoHousesFoundException, InvalidHouseIdException{
		List<Character> gryffyndorCharacters = new ArrayList<>();
		gryffyndorCharacters.add(harry);
		gryffyndorCharacters.add(hermione);
		gryffyndorCharacters.add(rony);
		List<String> validHouses = new ArrayList<>();
		validHouses.add("5a05e2b252f721a3cf2ea33f");
		when(houseFinder.findAllHouses()).thenReturn(validHouses);
		when(characterRepository.findCharactersByHouseId("5a05e2b252f721a3cf2ea33f")).thenReturn(gryffyndorCharacters);
		characterService.getAllCharactersFromGivenHouse("5a05e2b252f721a3cf2ea33f");
		verify(characterRepository, times(1)).findCharactersByHouseId("5a05e2b252f721a3cf2ea33f");
	}
	
	@Test
	@Tag("UnitTest")
	public void givenDbWithSeveralCharactersWhenGetCharactersByHouseWithInvalidHouseIdthenItShouldThrowInvalidHouseIdException() 
			throws InvalidHouseIdException, NoHousesFoundException{
		when(houseFinder.findAllHouses()).thenReturn(new ArrayList());
		Exception exception = assertThrows(InvalidHouseIdException.class, () -> {
			characterService.getAllCharactersFromGivenHouse("5a05e2b252f721a3cf2ea33f");
		});
		verify(houseFinder, times(1)).findAllHouses();
		
		String expectedMessage = "Invalid house id";
	    String actualMessage = exception.getMessage();
	    
		assertEquals(expectedMessage, actualMessage);
	}
	
	@Test
	@Tag("UnitTest")
	public void givenDbHasCharactersWhenGetAllCharactersThenItShouldCallRepository() {
		List<Character> characters = new ArrayList<>();
		characters.add(hermione);
		when(characterRepository.findAll()).thenReturn(characters);
		characterRepository.findAll();
		verify(characterRepository, times(1)).findAll();
	}
	
	@Test
	@Tag("UnitTest")
	public void givenDbNoCharactersWhenGetAllCharactersThenShouldThrowNoCharactersFoundException() {
		List<Character> characters = new ArrayList<>();
		when(characterRepository.findAll()).thenReturn(characters);
		Exception exception = assertThrows(NoCharactersFoundException.class , () -> {
			characterService.getAllCharacters();
		});
		String expectedMessage = "No characters found";
	    String actualMessage = exception.getMessage();
	    assertEquals(expectedMessage, actualMessage);
		
	}
	
	@Test
	@Tag("UnitTest")
	public void givenInvalidIdWhenDeleteteChracterByIdThenShouldThrowCharacterNotFoundException() {
		when(characterRepository.findById(1)).thenReturn(Optional.empty());
		Exception exception = assertThrows(CharacterNotFoundException.class, () -> {
			characterService.deleteCharacterById(1);
		});
		
		verify(characterRepository, times(1)).findById(1);
		String expectedMessage = "Character with id 1 not found";
	    String actualMessage = exception.getMessage();
	 
	    assertEquals(expectedMessage, actualMessage);
	}
	
	@Test
	@Tag("UnitTest")
	public void givenValidIdWhennDeleteteChracterByIdThenShouldCallRepository() {
		try {
			when(characterRepository.findById(1)).thenReturn(Optional.of(hermione));
			characterService.deleteCharacterById(1);
			verify(characterRepository, times(1)).delete(hermione);
		}catch (CharacterNotFoundException characterNotFoundEx) {
			characterNotFoundEx.printStackTrace();
		}
	}
	
	@Test
	@Tag("UnitTest")
	public void givenNoCharactersInDbWhenDeleteAllThenShouldThrowNoCharactersFoundException() {
		when(characterRepository.findAll()).thenReturn(new ArrayList<Character>());
		Exception exception = assertThrows(NoCharactersFoundException.class, () -> {
			characterService.deleteAllCharacters();
		});
		
		verify(characterRepository, times(1)).findAll();
		String expectedMessage = "No characters were found";
	    String actualMessage = exception.getMessage();
	 
	    assertEquals(expectedMessage, actualMessage);
		
	}
	
	@Test
	@Tag("UnitTest")
	public void givenDbHasCharactersWhenDeleteAllThenShouldCallRepository() throws NoCharactersFoundException{
		List<Character> characters = new ArrayList<>();
		characters.add(hermione);
		when(characterRepository.findAll()).thenReturn(characters);
		characterService.deleteAllCharacters();
		verify(characterRepository, times(1)).deleteAll();
	}
	
	@Test
	@Tag("UnitTest")
	public void givenCachingWhenCallingPotterApiThenItShouldCallItTwice() throws NoHousesFoundException, NoCharactersFoundException, InvalidHouseIdException {
		List<String> potterApiHouses = new ArrayList<String>() {{
			add("5a05e2b252f721a3cf2ea33f");
			add("5a05da69d45bd0a11bd5e06f");
			add("5a05dc8cd45bd0a11bd5e071");
			add("5a05dc58d45bd0a11bd5e070");
		}};
		List<Character> validCharacters = new ArrayList<Character>(){{
			add(hermione);
			add(rony);
			add(harry);
		}};
		when(houseFinder.findAllHouses()).thenReturn(potterApiHouses);
		when(characterRepository.findCharactersByHouseId("5a05e2b252f721a3cf2ea33f")).
		thenReturn(validCharacters);
		when(characterRepository.findCharactersByHouseId("5a05da69d45bd0a11bd5e06f")).
		thenReturn(validCharacters);
		characterService.getAllCharactersFromGivenHouse("5a05e2b252f721a3cf2ea33f");
		characterService.getAllCharactersFromGivenHouse("5a05da69d45bd0a11bd5e06f");
		verify(houseFinder, times(1)).findAllHouses();
	}
}
