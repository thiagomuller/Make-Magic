package com.thiagomuller.hpapi.controller.integration;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.thiagomuller.hpapi.controller.PotterApiHouses;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
@Transactional
public abstract class ControllerIntegrationTest {
	
	@MockBean
	protected PotterApiHouses potterApi;
	
	@LocalServerPort
	private int port;
	
	protected String characterUrl;
	
	@Autowired
	protected JsonHandlerForIntegrationTests jsonHandler;
	
	@Autowired
	protected HttpHandlerForIntegrationTests httpHandler;
	
	@BeforeEach
	public void setupCharacterUrl() {
		characterUrl = String.format("http://localhost:%d/characters", port);
	}

}
