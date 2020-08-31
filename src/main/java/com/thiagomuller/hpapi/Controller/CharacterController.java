package com.thiagomuller.hpapi.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thiagomuller.hpapi.Exception.CharacterNotFoundException;
import com.thiagomuller.hpapi.Exception.InvalidHouseIdException;
import com.thiagomuller.hpapi.Exception.NoCharactersFoundException;
import com.thiagomuller.hpapi.Model.Character;
import com.thiagomuller.hpapi.Service.CharacterService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/characters")
public class CharacterController {
	
	@Autowired
	private CharacterService characterService;
	
	@Operation(summary="Create a single character, character house id is validated at Potter Api:"
			+ "https://www.potterapi.com/")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "201", description = "Character created", 
				    content = { @Content(mediaType = "application/json", 
			      schema = @Schema(implementation = Character.class)) }),
			  @ApiResponse(responseCode = "400", description = "Invalid house id supplied")
	})
	@PostMapping
	public ResponseEntity createCharacter(@Valid @RequestBody Character character) throws InvalidHouseIdException{
		Character createdCharacter = characterService.createOrUpdateCharacter(character);
		return new ResponseEntity(createdCharacter, HttpStatus.CREATED);
	}
	
	@Operation(summary = "Update a single character, character house id is validated at Potter Api:"
			+ "https://www.potterapi.com/")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Character updated",
					content = { @Content(mediaType = "application/json",
					schema = @Schema(implementation = Character.class)) 
			}),
			@ApiResponse(responseCode = "400", description = "Invalid house id supplied")
	})
	@PutMapping
	public ResponseEntity updateCharacter(@Valid @RequestBody Character character) throws InvalidHouseIdException{
		Character updatedCharacter = characterService.createOrUpdateCharacter(character);
		return new ResponseEntity(updatedCharacter, HttpStatus.OK);
	}
	
	@Operation(summary = "Get a character by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "302", description = "Character found"),
			@ApiResponse(responseCode = "400", description = "No character found for this character id")
	})
	@GetMapping("/{characterId}")
	public ResponseEntity getChracterById(@PathVariable Integer characterId){
		Optional<Character> character = characterService.getCharacterById(characterId);
		if(character.isEmpty())
			return new ResponseEntity("No character found for this character id", HttpStatus.BAD_REQUEST);
		return new ResponseEntity(character, HttpStatus.FOUND);
	}
	
	@Operation(summary = "Get all characters")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "302", description = "Characters found"),
			@ApiResponse(responseCode = "400", description = "Invalid house id supplied"),
			@ApiResponse(responseCode = "404", description = "No characters found")
	})
	@GetMapping
	public ResponseEntity getAllCharacters(@RequestParam Optional<String> house) throws InvalidHouseIdException{
		List<Character> characters = new ArrayList<>();
		if(!house.isEmpty()) {
			characters = characterService.getAllCharactersFromGivenHouse(house.get());
			if(characters.isEmpty())
				return new ResponseEntity(characters, HttpStatus.NOT_FOUND);
			return new ResponseEntity(characters, HttpStatus.FOUND);
		}	
		characters = characterService.getAllCharacters();
		if(characters.isEmpty())
			return new ResponseEntity(characters, HttpStatus.NOT_FOUND);
		return new ResponseEntity(characters, HttpStatus.FOUND);
	}
	
	@Operation(summary = "Delete character by id")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Character deleted"),
			@ApiResponse(responseCode = "400", description = "Invalid character id supplied"),
			@ApiResponse(responseCode = "404", description = "Character not found")
	})
	@DeleteMapping("/{characterId}")
	public ResponseEntity deleteCharacterById(@PathVariable Integer characterId) throws CharacterNotFoundException{
		characterService.deleteCharacterById(characterId);
		return new ResponseEntity(String.format("Character %d deleted", characterId),
				HttpStatus.OK);
	}
	
	@Operation(summary = "Delete all characters")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Characters deleted"),
			@ApiResponse(responseCode = "404", description = "No characters found")
	})
	@DeleteMapping
	public ResponseEntity deleteAllCharacters() throws NoCharactersFoundException{
		characterService.deleteAllCharacters();
		return new ResponseEntity("Characters deleted", HttpStatus.OK);
	}
	
}
