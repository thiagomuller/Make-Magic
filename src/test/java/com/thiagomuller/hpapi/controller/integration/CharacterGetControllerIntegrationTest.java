package com.thiagomuller.hpapi.controller.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiagomuller.hpapi.HouseIdsProvider;
import com.thiagomuller.hpapi.exception.InvalidHouseIdException;
import com.thiagomuller.hpapi.exception.NoCharacterFoundException;
import com.thiagomuller.hpapi.exception.NoCharactersFoundException;
import com.thiagomuller.hpapi.model.Character;

public class CharacterGetControllerIntegrationTest extends ControllerIntegrationTest{
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	
	@Test
	@Tag("IntegrationTest")
	public void givenMulttipleCharactersInDbWhenGetAllCharactersCalledWithoutHouseThenItShouldReturnAllCharacters() 
			throws JSONException, NoCharacterFoundException, InvalidHouseIdException{
		List<String> validHouses = HouseIdsProvider.getHouseIds();
		when(potterApi.findAllHouses()).thenReturn(validHouses);
		String hermionePostingJson = jsonHandler.createCharacterJsonForPosting("Hermione Granger", "Student", "Hogwarts School of Witchcraft and Wizardry", 
				"5a05e2b252f721a3cf2ea33f", "Otter", "Muggle-Born");
		String harryPostingJson = jsonHandler.createCharacterJsonForPosting("Harry Potter", "Student", "Hogwarts School of Witchcraft and Wizardry", 
				"5a05e2b252f721a3cf2ea33f", "Deer", "Pure-Blood");
		String ronyPostingJson = jsonHandler.createCharacterJsonForPosting("Rony Weasley", "Student", "Hogwarts School of Witchcraft and Wizardry", 
				"5a05e2b252f721a3cf2ea33f", "Deer", "Pure-Blood");
		
		ResponseEntity<Character> postControllerResponse = httpHandler.postGivenJson(hermionePostingJson, characterUrl);
		assertEquals(HttpStatus.CREATED, postControllerResponse.getStatusCode());
		postControllerResponse = httpHandler.postGivenJson(harryPostingJson, characterUrl);
		assertEquals(HttpStatus.CREATED, postControllerResponse.getStatusCode());
		postControllerResponse = httpHandler.postGivenJson(ronyPostingJson, characterUrl);
		assertEquals(HttpStatus.CREATED, postControllerResponse.getStatusCode());
		
		ResponseEntity<List<Character>> getAllCharactersControllerResponse = httpHandler.getAllCharacters(characterUrl);
		assertEquals(HttpStatus.FOUND, getAllCharactersControllerResponse.getStatusCode());
		assertEquals(getAllCharactersControllerResponse.getBody().size(), 3);		
	}
	
	@Test
	@Tag("IntegrationTest")
	public void givenNoCharactersInDbWhenGetAllCharactersCalledWithoutHouseThenShouldThrowNoCharactersFoundException() {
		ResponseEntity<String> getAllCharactersResponse = httpHandler.getAllCharactersForEmptyDbScenario(characterUrl);
		assertEquals(HttpStatus.NOT_FOUND, getAllCharactersResponse.getStatusCode());
	}
	
	@Test
	@Tag("IntegrationTest")
	public void givenCharactersFromDifferentHousesInDbWhenGetCharactersByHouseIdGetsCalledWithValidHouseIdThenItShouldReturnCharactersFromThatHouse() 
			throws JSONException, NoCharactersFoundException, InvalidHouseIdException{
		List<String> validHouses = HouseIdsProvider.getHouseIds();
		when(potterApi.findAllHouses()).thenReturn(validHouses);
		String hermionePostingJson = jsonHandler.createCharacterJsonForPosting("Hermione Granger", "Student", "Hogwarts School of Witchcraft and Wizardry", 
				"5a05e2b252f721a3cf2ea33f", "Otter", "Muggle-Born");
		String harryPostingJson = jsonHandler.createCharacterJsonForPosting("Severus Snape", "Professor", "Hogwarts School of Witchcraft and Wizardry", 
				"5a05dc8cd45bd0a11bd5e071", "Doe", "Pure-Blood");
		String ronyPostingJson = jsonHandler.createCharacterJsonForPosting("Rony Weasley", "Student", "Hogwarts School of Witchcraft and Wizardry", 
				"5a05e2b252f721a3cf2ea33f", "Deer", "Pure-Blood");

		ResponseEntity<Character> postControllerResponse = httpHandler.postGivenJson(hermionePostingJson, characterUrl);
		assertEquals(HttpStatus.CREATED, postControllerResponse.getStatusCode());
		postControllerResponse = httpHandler.postGivenJson(harryPostingJson, characterUrl);
		assertEquals(HttpStatus.CREATED, postControllerResponse.getStatusCode());
		postControllerResponse = httpHandler.postGivenJson(ronyPostingJson, characterUrl);
		assertEquals(HttpStatus.CREATED, postControllerResponse.getStatusCode());
		
		
		ResponseEntity<List<Character>> charactersFromGivenHouseResponse = httpHandler.getAllCharactersFromGivenValidHouseId("5a05e2b252f721a3cf2ea33f", characterUrl);
		assertEquals(HttpStatus.FOUND, charactersFromGivenHouseResponse.getStatusCode());
		assertEquals(charactersFromGivenHouseResponse.getBody().size(), 2);
		charactersFromGivenHouseResponse = httpHandler.getAllCharactersFromGivenValidHouseId("5a05dc8cd45bd0a11bd5e071", characterUrl);
		assertEquals(charactersFromGivenHouseResponse.getBody().size(), 1);
		
	}
	
	@Test
	@Tag("IntegrationTest")
	public void givenCharactersFromDifferentHousesOnDbWhenGetCharactersByHouseIdGetsCalledWithInValidHouseIdThenItShouldReturnHttp400() 
			throws JSONException, InvalidHouseIdException{
		List<String> validHouses = HouseIdsProvider.getHouseIds();
		when(potterApi.findAllHouses()).thenReturn(validHouses);
		String hermionePostingJson = jsonHandler.createCharacterJsonForPosting("Hermione Granger", "Student", "Hogwarts School of Witchcraft and Wizardry", "5a05e2b252f721a3cf2ea33f", "Otter", "Muggle-Born");
		String harryPostingJson = jsonHandler.createCharacterJsonForPosting("Severus Snape", "Professor", "Hogwarts School of Witchcraft and Wizardry", "5a05dc8cd45bd0a11bd5e071", "Doe", "Pure-Blood");
		String ronyPostingJson = jsonHandler.createCharacterJsonForPosting("Rony Weasley", "Student", "Hogwarts School of Witchcraft and Wizardry", "5a05e2b252f721a3cf2ea33f", "Deer", "Pure-Blood");

		ResponseEntity<Character> postControllerResponse = httpHandler.postGivenJson(hermionePostingJson, characterUrl);
		assertEquals(HttpStatus.CREATED, postControllerResponse.getStatusCode());
		postControllerResponse = httpHandler.postGivenJson(harryPostingJson, characterUrl);
		assertEquals(HttpStatus.CREATED, postControllerResponse.getStatusCode());
		postControllerResponse = httpHandler.postGivenJson(ronyPostingJson, characterUrl);
		assertEquals(HttpStatus.CREATED, postControllerResponse.getStatusCode());
		
		ResponseEntity<String> getAllCharactersResponse = httpHandler.getAllCharactersFromGivenInvalidHouseId("lala", characterUrl);
		assertEquals(HttpStatus.BAD_REQUEST, getAllCharactersResponse.getStatusCode());
	}
	
	@Test
	@Tag("IntegrationTest")
	public void givenCharacterFromAGivenIdInDbWhenGetCharacterByIdWithValidIdCalledThenItShouldReturnIt() 
			throws JsonProcessingException, JSONException, InvalidHouseIdException{
		List<String> validHouses = HouseIdsProvider.getHouseIds();
		when(potterApi.findAllHouses()).thenReturn(validHouses);
		String hermionePostingJson = jsonHandler.createCharacterJsonForPosting("Hermione Granger", "Student", "Hogwarts School of Witchcraft and Wizardry", "5a05e2b252f721a3cf2ea33f", "Otter", "Muggle-Born");
		ResponseEntity<Character> postControllerResponse = httpHandler.postGivenJson(hermionePostingJson, characterUrl);
		assertEquals(HttpStatus.CREATED, postControllerResponse.getStatusCode());
		
		String hermioneExpectedJson = jsonHandler.createCharacterJsonForUpdateOrExpectedResponse(1, "Hermione Granger", 
				"Student", "Hogwarts School of Witchcraft and Wizardry", "5a05e2b252f721a3cf2ea33f", "Otter", "Muggle-Born");
		ResponseEntity<Character> getByIdControllerResponse = httpHandler.getCharacterByIdWithValidId(1, characterUrl);
		assertEquals(HttpStatus.FOUND, getByIdControllerResponse.getStatusCode());
		assertEquals(hermioneExpectedJson, mapper.writeValueAsString(getByIdControllerResponse.getBody()));
	}
	
	@Test
	@Tag("IntegrationTest")
	public void givenNoCharacterInDbWhenGetCharacterByIdWithValidIdCalledThenItShouldReturnHttp40$() {
		ResponseEntity<String> getCharacterByIdControllerResponse = httpHandler.getCharacterByIdWithNoCharacterInDb(1, characterUrl);
		assertEquals(HttpStatus.NOT_FOUND, getCharacterByIdControllerResponse.getStatusCode());
	}
	

}
