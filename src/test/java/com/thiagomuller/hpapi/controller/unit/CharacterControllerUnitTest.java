package com.thiagomuller.hpapi.controller.unit;


import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiagomuller.hpapi.CharacterProvider;
import com.thiagomuller.hpapi.controller.CharacterController;
import com.thiagomuller.hpapi.controller.integration.JsonHandlerForIntegrationTests;
import com.thiagomuller.hpapi.exception.InvalidHouseIdException;
import com.thiagomuller.hpapi.model.Character;
import com.thiagomuller.hpapi.service.CharacterService;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test", "unittest"})
public class CharacterControllerUnitTest {
	
	@MockBean
	public CharacterService service;
	
	@Autowired
	CharacterController controller;
	
	@Autowired
	JsonHandlerForIntegrationTests jsonHandler;
	
	private static ObjectMapper mapper = new ObjectMapper();
	
	@Test
	@Tag("UnitTest")
	public void givenExistingCharacterWhenGetCharacterByIdCalledThenShouldReturnHttp302() throws JSONException, JsonProcessingException{
		Character hermione = CharacterProvider.getHermione();
		hermione.setCharacterId(1);
		Optional<Character> characterFromService = Optional.of(hermione);
		String hermioneJson = jsonHandler.createCharacterJsonForUpdateOrExpectedResponse
				(1, "Hermione Granger", "Student", "Hogwarts School of Witchcraft and Wizardry", 
						"5a05e2b252f721a3cf2ea33f", "Otter", "Muggle-Born");
		when(service.getCharacterById(1)).thenReturn(characterFromService);
		ResponseEntity controllerResponse = controller.getChracterById(1);
		verify(service, times(1)).getCharacterById(1);
		assertEquals(HttpStatus.FOUND, controllerResponse.getStatusCode());
		assertEquals(hermioneJson, mapper.writeValueAsString(controllerResponse.getBody()));
	}
	
	@Test
	@Tag("UnitTest")
	public void givenNonExistingCharacterWhenGetCharacterByidCalledThenShouldReturnHttp404() throws JsonProcessingException{
		when(service.getCharacterById(1)).thenReturn(Optional.empty());
		ResponseEntity controllerResponse = controller.getChracterById(1);
		verify(service, times(1)).getCharacterById(1);
		assertEquals(HttpStatus.NOT_FOUND, controllerResponse.getStatusCode());
		assertEquals("\"No character found for this character id\"", 
				mapper.writeValueAsString(controllerResponse.getBody()));
	}
	
	@Test
	@Tag("UnitTest")
	public void givenCharactersExistWhenCallingGetAllCharactersWithValidHouseParamThenShouldReturnHttp302() throws InvalidHouseIdException{
		Character hermione = CharacterProvider.getHermione();
		Character harry = CharacterProvider.getHarry();
		List<Character> foundCharacters = new ArrayList<Character>() {{
			add(harry);
			add(hermione);
		}};
		when(service.getAllCharactersFromGivenHouse("5a05e2b252f721a3cf2ea33f")).thenReturn(foundCharacters);
		ResponseEntity controllerResponse = controller.getAllCharacters(Optional.of("5a05e2b252f721a3cf2ea33f"));
		verify(service, times(1)).getAllCharactersFromGivenHouse("5a05e2b252f721a3cf2ea33f");
		assertEquals(HttpStatus.FOUND, controllerResponse.getStatusCode());
	}
	
	@Test
	@Tag("UnitTest")
	public void givenCharactersExistWhenGetAllCharactersCalledWithoutHouseParamThenItShouldReturnHttp302() throws InvalidHouseIdException{
		Character hermione = CharacterProvider.getHermione();
		Character harry = CharacterProvider.getHarry();
		List<Character> foundCharacters = new ArrayList<Character>() {{
			add(harry);
			add(hermione);
		}};
		when(service.getAllCharacters()).thenReturn(foundCharacters);
		ResponseEntity controllerResponse = controller.getAllCharacters(Optional.empty());
		verify(service, times(1)).getAllCharacters();
		assertEquals(HttpStatus.FOUND, controllerResponse.getStatusCode());
	}
}
