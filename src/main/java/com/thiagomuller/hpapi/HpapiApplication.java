package com.thiagomuller.hpapi;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationListener;

@SpringBootApplication
@EnableCaching
public class HpapiApplication {
	
  static class EnvironmentPrepared implements ApplicationListener<ApplicationEnvironmentPreparedEvent>{
	  
	    @Override
	    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
	      Map<String, String> requiredEnvVariables = new HashMap<>();
	      requiredEnvVariables.put("apiKey", event.getEnvironment().
	    		  getProperty("potter-api.apiKey", String.class, "")); 
	      requiredEnvVariables.put("apiHouseUrl", event.getEnvironment().getProperty
	    		  ("potter-api.houseUrl", String.class, ""));
	      requiredEnvVariables.put("dbUsername", event.getEnvironment().getProperty
	    		  ("spring.datasource.username", String.class, ""));
	      requiredEnvVariables.put("dbPassword", event.getEnvironment().
	    		  getProperty("spring.datasource.password", String.class, ""));
	      requiredEnvVariables.put("dbUrl", event.getEnvironment().
	    		  getProperty("spring.datasource.url", String.class, ""));
	      requiredEnvVariables.put("numberOfRetries", event.getEnvironment().
	    		  getProperty("potter-api.numberOfRetries", String.class, ""));
	      for(Map.Entry<String, String> envVariable  : requiredEnvVariables.entrySet()) {
	    	  if(envVariable.getValue().isEmpty())
	    		  throw new RuntimeException("COULD NOT START APPLICATION DUE TO MISSING ENVIRONMENT VARIABLES, PLEASE TAKE A LOOK AT WHICH VARIABLES SHOULD BE SET ON THE README OF THE PROJECT");
	      }
	    }
	  };

	public static void main(String[] args) throws Exception{
		SpringApplication springApplication = new SpringApplication(HpapiApplication.class);
		springApplication.addListeners(new HpapiApplication.EnvironmentPrepared());
	    springApplication.run(args);
	}

}
