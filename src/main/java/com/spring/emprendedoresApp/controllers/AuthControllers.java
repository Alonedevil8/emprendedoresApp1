package com.spring.emprendedoresApp.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.emprendedoresApp.models.dtos.LoginDTO;
import com.spring.emprendedoresApp.models.dtos.ResponseDTO;
import com.spring.emprendedoresApp.persistence.entities.UserEntity;
import com.spring.emprendedoresApp.services.IAuthService;


@RestController
@RequestMapping("/auth")
public class AuthControllers {

	@Autowired
	IAuthService authService;

	@PostMapping("/register")
	private ResponseEntity<ResponseDTO> register(@RequestBody UserEntity user) throws Exception {
	    String roleName = "ROLE_USER";  // Siempre asignando "role_user"
	    return new ResponseEntity<>(authService.register(user, roleName), HttpStatus.CREATED);
	}


	@PostMapping("/login")
	private ResponseEntity<HashMap<String, String>> login(@RequestBody LoginDTO loginRequest) throws Exception{
		
	HashMap<String, String> login = authService.login(loginRequest);
		
		if(login.containsKey("jwt")) {
			return new ResponseEntity<>(login, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(login, HttpStatus.UNAUTHORIZED);
		}
	}
}
