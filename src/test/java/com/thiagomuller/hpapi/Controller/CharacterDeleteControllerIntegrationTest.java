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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiagomuller.hpapi.Exception.InvalidHouseIdException;
import com.thiagomuller.hpapi.Exception.NoHousesFoundException;
import com.thiagomuller.hpapi.Model.Character;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class CharacterDeleteControllerIntegrationTest {
	
	@Autowired
	private PotterApiHouses potterApi;
	
	@Autowired
	private JsonHandlerForIntegrationTests jsonHandler;
	
	@Autowired
	private HttpHandlerForIntegrationTests httpHandler;
	
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
	public void givenCharacterPresentWhenDeleteByIdWithValidIdThenShouldReturnHttp200() throws JSONException, NoHousesFoundException, InvalidHouseIdException{
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
	public void givenDbHasCharactersWhenDeleteAllCharactersThenShouldReturnHttp200() throws JSONException,  NoHousesFoundException, InvalidHouseIdException{
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
