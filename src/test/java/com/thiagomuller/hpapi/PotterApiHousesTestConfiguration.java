package com.thiagomuller.hpapi;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.thiagomuller.hpapi.Controller.PotterApiHouses;

@Profile("test")
@Configuration
public class PotterApiHousesTestConfiguration {
	
	@Bean
	@Primary
	public PotterApiHouses potterApi() {
		return Mockito.mock(PotterApiHouses.class);
	}
}
