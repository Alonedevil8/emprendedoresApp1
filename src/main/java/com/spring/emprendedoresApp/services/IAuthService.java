package com.spring.emprendedoresApp.services;

import java.util.HashMap;

import com.spring.emprendedoresApp.models.dtos.LoginDTO;
import com.spring.emprendedoresApp.models.dtos.ResponseDTO;
import com.spring.emprendedoresApp.persistence.entities.UserEntity;

public interface IAuthService {
	
	public HashMap<String, String> login(LoginDTO login) throws Exception;

	public ResponseDTO register(UserEntity user, String roleName) throws Exception;
}

