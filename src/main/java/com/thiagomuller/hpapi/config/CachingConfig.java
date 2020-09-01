package com.thiagomuller.hpapi.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile({"!test", "!unittest"})
@EnableCaching
@Configuration
public class CachingConfig {

}
