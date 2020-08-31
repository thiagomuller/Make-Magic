package com.thiagomuller.hpapi.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.thiagomuller.hpapi.Model.Character;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
@Profile("!unittest")
public class HttpHandlerForIntegrationTests {
	
	@Autowired
	private TestRestTemplate restTemplate;
	
	public ResponseEntity<Character> postGivenJson(String jsonToBePosted, String url) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(jsonToBePosted.toString(), headers);
		return restTemplate.postForEntity(url, request, Character.class);
	}
	
	public ResponseEntity<Character> putGivenJson(String jsonToBeUpdated, String url){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(jsonToBeUpdated.toString(), headers);
		return restTemplate.exchange(url, HttpMethod.PUT, request, Character.class);
	}
	
	public ResponseEntity<List<Character>> getAllCharacters(String url){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(headers);
		return restTemplate.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<List<Character>>() {});
	}
	
	public ResponseEntity<String> getAllCharactersForEmptyDbScenario(String url){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(headers);
		return restTemplate.exchange(url, HttpMethod.GET, request, new ParameterizedTypeReference<String>() {});
	}
	
	public ResponseEntity<List<Character>> getAllCharactersFromGivenValidHouseId(String houseId, String url){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("house", houseId);

		return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request, new ParameterizedTypeReference<List<Character>>() {});

	}
	
	public ResponseEntity<String> getAllCharactersFromGivenInvalidHouseId(String houseId, String url){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.queryParam("house", houseId);
		return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request,  new ParameterizedTypeReference<String>() {});
	}
	
	public ResponseEntity<Character> getCharacterByIdWithValidId(Integer characterId, String url){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.path(String.format("/%d", characterId));
		return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request,  new ParameterizedTypeReference<Character>() {});
	}
	
	public ResponseEntity<String> getCharacterByIdWithNoCharacterInDb(Integer characterId, String url){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(headers);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
				.path(String.format("/%d", characterId));
		return restTemplate.exchange(builder.toUriString(), HttpMethod.GET, request,  new ParameterizedTypeReference<String>() {});
	}
	
	public ResponseEntity<String> deleteCharacterById(Integer characterId, String url){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(headers);
		return restTemplate.exchange(String.format(url+"/%d", characterId), HttpMethod.DELETE, request, String.class);
	}
	
	public ResponseEntity<String> deleteAllCharacters(String url){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<String>(headers);
		return restTemplate.exchange(url, HttpMethod.DELETE, request, String.class);
	}

}
