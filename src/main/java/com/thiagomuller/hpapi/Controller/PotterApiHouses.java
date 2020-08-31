package com.thiagomuller.hpapi.Controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiagomuller.hpapi.Config.PotterApiConfiguration;
import com.thiagomuller.hpapi.Exception.NoHousesFoundException;
import com.thiagomuller.hpapi.Model.House;
import com.thiagomuller.hpapi.Service.HouseFinder;

import lombok.extern.java.Log;

@Component
@Log
public class PotterApiHouses implements HouseFinder{
	
	@Autowired
	PotterApiConfiguration potterApiConfig;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private Integer retriesDoneSoFar = 0;
	

	@Override
	public List<String> findAllHouses() throws NoHousesFoundException{			
		List<String> potterApiHouseIds = new ArrayList<>();
		List<House> potterApiHouses = handleHttpCallToPotterApi();
		if(potterApiHouses.isEmpty())
			throw new NoHousesFoundException("No houses were found at Potter api");
		for(House house : potterApiHouses)
			potterApiHouseIds.add(house.getHouseId());
		return potterApiHouseIds;	
	}
	
	private List<House> handleHttpCallToPotterApi() throws NoHousesFoundException{
		Integer numberOfRetries = Integer.valueOf(potterApiConfig.getNumberOfRetries());
		HttpResponse<String> potterApiResponse = callPotterApi(numberOfRetries);
		try {
			return mapper.readValue(potterApiResponse.body(), new TypeReference<List<House>>() {});
		}catch (JsonProcessingException json) {
			log.info(String.format("COULDN'T PARSE JSON SENT FROM POTTER API, NUMBER OF RETRIES AVAILABLE: %d,", 
					numberOfRetries - retriesDoneSoFar));
			if(retriesDoneSoFar < numberOfRetries)
				callPotterApi(numberOfRetries);
		}
		throw new NoHousesFoundException("No houses were found at Potter api");
	}
	
	private HttpResponse<String> callPotterApi(Integer numberOfRetries) throws NoHousesFoundException{
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder(URI.create(potterApiConfig.getHouseUrl() 
				+ potterApiConfig.getApiKey())).build();
		try {
			HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
			if(response.statusCode() > 400 && retriesDoneSoFar < numberOfRetries) {
				log.info(String.format("HTTP STATUS CODE FROM POTTER API: %d,", response.statusCode(), " RETRIES AVAILABLE: %d", 
						numberOfRetries - retriesDoneSoFar));
				retriesDoneSoFar ++;
				return callPotterApi(numberOfRetries);
			}
			return response;
				
		}catch (InterruptedException interrupted){
			interrupted.printStackTrace();
		}catch (IOException io) {
			io.printStackTrace();
		}
		throw new NoHousesFoundException("No houses were found at Potter api");
	}
}
