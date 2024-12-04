package com.spring.emprendedoresApp.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SimpleGrantedAuthorityJsonCreator {

	@JsonCreator
	public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role) {
	}
}
