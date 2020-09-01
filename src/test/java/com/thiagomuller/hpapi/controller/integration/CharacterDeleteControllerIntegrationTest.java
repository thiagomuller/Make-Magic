package com.thiagomuller.hpapi.controller.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiagomuller.hpapi.HouseIdsProvider;
import com.thiagomuller.hpapi.exception.InvalidHouseIdException;
import com.thiagomuller.hpapi.model.Character;

public class CharacterDeleteControllerIntegrationTest extends ControllerIntegrationTest{
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	@Test
	@Tag("IntegrationTest")
	public void givenCharacterPresentWhenDeleteByIdWithValidIdThenShouldReturnHttp200() throws JSONException, InvalidHouseIdException{
		List<String> validHouses = HouseIdsProvider.getHouseIds();
		when(potterApi.findAllHouses()).thenReturn(validHouses);
		String hermionePostingJson = jsonHandler.createCharacterJsonForPosting("Hermione Granger", "Student", "Hogwarts School of Witchcraft and Wizardry", 
				"5a05e2b252f721a3cf2ea33f", "Otter", "Muggle-Born");
		ResponseEntity<Character> postControllerResponse = httpHandler.postGivenJson(hermionePostingJson, characterUrl);
		assertEquals(HttpStatus.CREATED, postControllerResponse.getStatusCode());
		ResponseEntity<String> deleteControllerResponse = httpHandler.deleteCharacterById(1, characterUrl);
		assertEquals(HttpStatus.OK, deleteControllerResponse.getStatusCode());
	}
	
	@Test
	@Tag("IntegrationTest")
	public void givenNoCharacterInDbWhenDeleteByIdWithInvalidIdThenShouldReturnHttp400() throws JSONException{
		ResponseEntity<String> deleteControllerResponse = httpHandler.deleteCharacterById(1, characterUrl);
		assertEquals(HttpStatus.BAD_REQUEST, deleteControllerResponse.getStatusCode());
	}
	
	@Test
	@Tag("IntegrationTest")
	public void givenDbHasCharactersWhenDeleteAllCharactersThenShouldReturnHttp200() throws JSONException, InvalidHouseIdException{
		List<String> validHouses = HouseIdsProvider.getHouseIds();
		when(potterApi.findAllHouses()).thenReturn(validHouses);
		String hermionePostingJson = jsonHandler.createCharacterJsonForPosting("Hermione Granger", "Student", "Hogwarts School of Witchcraft and Wizardry", 
				"5a05e2b252f721a3cf2ea33f", "Otter", "Muggle-Born");
		ResponseEntity<Character> postControllerResponse = httpHandler.postGivenJson(hermionePostingJson, characterUrl);
		assertEquals(HttpStatus.CREATED, postControllerResponse.getStatusCode());
		ResponseEntity<String> deleteControllerResponse = httpHandler.deleteAllCharacters(characterUrl);
		assertEquals(HttpStatus.OK, deleteControllerResponse.getStatusCode());
	}
	
	@Test
	@Tag("IntegrationTest")
	public void givenNoCharacterInDbWhenDeleteAllCharactersThenShouldReturnHttp404() throws JSONException{
		ResponseEntity<String> deleteControllerResponse = httpHandler.deleteAllCharacters(characterUrl);
		assertEquals(HttpStatus.NOT_FOUND, deleteControllerResponse.getStatusCode());
	}

}
