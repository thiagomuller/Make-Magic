package com.thiagomuller.hpapi.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiagomuller.hpapi.Exception.InvalidHouseIdException;
import com.thiagomuller.hpapi.Exception.NoHousesFoundException;
import com.thiagomuller.hpapi.Model.Character;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CharacterPostControllerIntegrationTest {
	
	@Autowired
	private PotterApiHouses potterApi;
	
	@Autowired
	JsonHandlerForIntegrationTests jsonHandler;
	
	@Autowired
	HttpHandlerForIntegrationTests httpHandler;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	private static String characterUrl = "http://localhost:8080/characters";
	
	private static List<String> validHouses = new ArrayList<String>() {{
		add("5a05e2b252f721a3cf2ea33f");
		add("5a05da69d45bd0a11bd5e06f");
		add("5a05dc8cd45bd0a11bd5e071");
		add("5a05dc58d45bd0a11bd5e070");
	}};
	
	@Test
	@Tag("IntegrationTest")
	public void givenAValidCharacterJsonWhenCallingCharacterPostThenShouldCreateCharacterAndReturnIt() throws JsonProcessingException, JSONException, NoHousesFoundException, InvalidHouseIdException{
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
	public void givenAnInvalidCharacterJsonWhenCallingCharacterPostThenShouldReturnHttpStatus400() throws JsonProcessingException, JSONException{
		String hermioneJson = jsonHandler.createCharacterJsonForPosting("Herm!one Gr@nger", "Student", "Hogwarts School of Witchcraft and Wizardry", "5a05e2b252f721a3cf2ea33f", "Otter", "Muggle-Born");
		ResponseEntity<Character> controllerResponse = httpHandler.postGivenJson(hermioneJson, characterUrl);
		assertEquals(HttpStatus.BAD_REQUEST, controllerResponse.getStatusCode());

	}

}