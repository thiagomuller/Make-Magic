package com.thiagomuller.hpapi.controller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thiagomuller.hpapi.config.PotterApiConfiguration;
import com.thiagomuller.hpapi.model.House;
import com.thiagomuller.hpapi.service.HouseFinder;

import lombok.extern.java.Log;

@Component
@Log
public class PotterApiHouses implements HouseFinder{
	
	@Autowired
	PotterApiConfiguration potterApiConfig;
	
	private ObjectMapper mapper = new ObjectMapper();
	
	private Integer retriesDoneSoFar = 0;
	
	
	@Cacheable("houses")
	@Override
	public List<String> findAllHouses(){
		List<String> potterApiHouseIds = new ArrayList<>();
		List<House> potterApiHouses = getHousesListFromApi();
		for(House house : potterApiHouses)
			potterApiHouseIds.add(house.getHouseId());
		return potterApiHouseIds;	
	}
	
	private List<House> getHousesListFromApi(){
		Integer numberOfRetries = Integer.valueOf(potterApiConfig.getNumberOfRetries());
		HttpResponse<String> potterApiResponse = callPotterApi(numberOfRetries);
		if(potterApiResponse == null)
			return new ArrayList<House>();
		try {
			return mapper.readValue(potterApiResponse.body(), new TypeReference<List<House>>() {});
		}catch (JsonProcessingException json) {
			log.info(String.format("COULDN'T PARSE JSON SENT FROM POTTER API, NUMBER OF RETRIES AVAILABLE: %d,", 
					numberOfRetries - retriesDoneSoFar));
			if(retriesDoneSoFar < numberOfRetries)
				callPotterApi(numberOfRetries);
		}
		return new ArrayList<House>();
	}
	
	private HttpResponse<String> callPotterApi(Integer numberOfRetries){
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
		return null;
	}
}
