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
import com.thiagomuller.hpapi.controller.PotterApiHouses;
import com.thiagomuller.hpapi.exception.InvalidHouseIdException;
import com.thiagomuller.hpapi.model.Character;

public class CharacterPostControllerIntegrationTest extends ControllerIntegrationTest{
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	@Test
	@Tag("IntegrationTest")
	public void givenAValidCharacterJsonWhenCallingCharacterPostThenShouldCreateCharacterAndReturnIt() 
			throws JsonProcessingException, JSONException,InvalidHouseIdException{
		List<String> validHouses = HouseIdsProvider.getHouseIds();
		when(potterApi.findAllHouses()).thenReturn(validHouses);
		String hermioneJson = jsonHandler.createCharacterJsonForPosting("Hermione Granger", "Student", "Hogwarts School of Witchcraft and Wizardry", "5a05e2b252f721a3cf2ea33f", "Otter", "Muggle-Born");
		ResponseEntity<Character> controllerResponse = httpHandler.postGivenJson(hermioneJson, characterUrl);
		String hermioneExpectedResponseJson = jsonHandler.createCharacterJsonForUpdateOrExpectedResponse(1, "Hermione Granger", "Student", "Hogwarts School of Witchcraft and Wizardry", "5a05e2b252f721a3cf2ea33f", 
				"Otter", "Muggle-Born");
		assertEquals(HttpStatus.CREATED, controllerResponse.getStatusCode());
		assertEquals(hermioneExpectedResponseJson, mapper.writeValueAsString(controllerResponse.getBody()));
		
	}
	
	@Test
	@Tag("IntegrationTest")
	public void givenAnInvalidCharacterJsonWhenCallingCharacterPostThenShouldReturnHttpStatus400() 
			throws JsonProcessingException, JSONException{
		String hermioneJson = jsonHandler.createCharacterJsonForPosting("Herm!one Gr@nger", "Student", "Hogwarts School of Witchcraft and Wizardry", "5a05e2b252f721a3cf2ea33f", "Otter", "Muggle-Born");
		ResponseEntity<Character> controllerResponse = httpHandler.postGivenJson(hermioneJson, characterUrl);
		assertEquals(HttpStatus.BAD_REQUEST, controllerResponse.getStatusCode());

	}

}
