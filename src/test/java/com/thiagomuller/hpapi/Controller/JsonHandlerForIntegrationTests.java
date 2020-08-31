package com.thiagomuller.hpapi.Controller;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class JsonHandlerForIntegrationTests {
	public String createCharacterJsonForPosting(String name, String role, String school, String houseId, String patronus, String bloodStatus) throws JSONException{
		JSONObject characterPostingJson = new JSONObject();
		characterPostingJson.put("name", name);
		characterPostingJson.put("role", role);
		characterPostingJson.put("school", school);
		characterPostingJson.put("houseId", houseId);
		characterPostingJson.put("patronus", patronus);
		characterPostingJson.put("bloodStatus", bloodStatus);
		return characterPostingJson.toString();
	}
	
	public String createCharacterJsonForUpdateOrExpectedResponse(Integer characterId, String name, String role, String school, String houseId, String patronus, String bloodStatus) 
			throws JSONException{
		JSONObject characterPutJson = new JSONObject();
		characterPutJson.put("characterId", 1);
		characterPutJson.put("name", name);
		characterPutJson.put("role", role);
		characterPutJson.put("school", school);
		characterPutJson.put("houseId", houseId);
		characterPutJson.put("patronus", patronus);
		characterPutJson.put("bloodStatus", bloodStatus);
		return characterPutJson.toString();
	}
}
