package com.spring.emprendedoresApp.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.emprendedoresApp.models.dtos.LoginDTO;
import com.spring.emprendedoresApp.models.dtos.ResponseDTO;
import com.spring.emprendedoresApp.models.validation.UserValidation;
import com.spring.emprendedoresApp.persistence.entities.RoleEntity;
import com.spring.emprendedoresApp.persistence.entities.UserEntity;
import com.spring.emprendedoresApp.persistence.repositories.UserRepository;
import com.spring.emprendedoresApp.persistence.repositories.RoleRepository;
import com.spring.emprendedoresApp.services.IAuthService;
import com.spring.emprendedoresApp.services.IJWTUtilityService;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private IJWTUtilityService jwtUtilityService;

    @Autowired
    private UserValidation userValidation;

    @Override
    public HashMap<String, String> login(LoginDTO login) throws Exception {
        try {
            HashMap<String, String> jwt = new HashMap<>();
            Optional<UserEntity> user = userRepository.findByEmail(login.getEmail());

            if (user.isEmpty()) {
                jwt.put("error", "User not Registered!");
                return jwt;
            }

            if (verifyPassword(login.getPassword(), user.get().getPassword())) {
                // Aquí se obtiene el nombre del rol como String
                String roleName = user.get().getRole().getRoleName().name();  // Accede al enum directamente

                jwt.put("jwt", jwtUtilityService.generateJWT(user.get(), List.of(roleName)));
            } else {
                jwt.put("error", "Authentication Failed");
            }
            return jwt;
        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }


    @Override
    public ResponseDTO register(UserEntity user, String roleName) throws Exception {
        try {
            ResponseDTO response = userValidation.validate(user);

            if (response.getNumOfError() > 0) {
                return response;
            }

            // Validación de usuarios duplicados por email
            Optional<UserEntity> existingUser = userRepository.findByEmail(user.getEmail());
            if (existingUser.isPresent()) {
                response.setNumOfError(1);
                response.setMessage("El correo ya está registrado!");
                return response;
            }

            // Validación de usuarios duplicados por username (si es necesario)
            Optional<UserEntity> existingUsername = userRepository.findByUsername(user.getUsername());
            if (existingUsername.isPresent()) {
                response.setNumOfError(1);
                response.setMessage("El nombre de usuario ya está registrado!");
                return response;
            }

            // Validar y asignar el rol recibido en la URL
            RoleEntity.RoleName roleEnum;
            try {
                roleEnum = RoleEntity.RoleName.valueOf(roleName.toUpperCase());
            } catch (IllegalArgumentException e) {
                response.setNumOfError(1);
                response.setMessage("Rol no válido: " + roleName);
                return response;
            }

            // Buscar el rol correspondiente en la base de datos
            RoleEntity role = roleRepository.findByRoleName(roleEnum);
            if (role == null) {
                response.setNumOfError(1);
                response.setMessage("Rol no encontrado en la base de datos");
                return response;
            }

            user.setRole(role);

            // Encriptar la contraseña
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            user.setPassword(encoder.encode(user.getPassword()));

            // Guardar el usuario con el rol asignado
            userRepository.save(user);

            response.setMessage("Usuario creado exitosamente!");
            return response;

        } catch (Exception e) {
            throw new Exception(e.toString());
        }
    }

    private boolean verifyPassword(String enteredPassword, String storedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(enteredPassword, storedPassword);
    }
}
