package com.thiagomuller.hpapi.Config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("potter-api")
@Validated
@Getter
@Setter
public class PotterApiConfiguration {
	private String houseUrl;
	private String apiKey;
	private String numberOfRetries;
}
