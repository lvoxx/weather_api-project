package com.skyapi.weatherapiservice.app;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan("com.skyapi.weatherapicommon")
public class AppConfig {
    
}
