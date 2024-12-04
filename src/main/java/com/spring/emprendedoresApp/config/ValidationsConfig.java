package com.spring.emprendedoresApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.spring.emprendedoresApp.models.validation.UserValidation;

@Configuration
public class ValidationsConfig {

	@Bean
	public UserValidation userValidation() {

		return new UserValidation();
	}
}
