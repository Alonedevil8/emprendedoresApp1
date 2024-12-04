package com.spring.emprendedoresApp.services.impl;

import com.spring.emprendedoresApp.models.dtos.ResponseDTO;
import com.spring.emprendedoresApp.models.validation.UserValidation;
import com.spring.emprendedoresApp.persistence.entities.RoleEntity;
import com.spring.emprendedoresApp.persistence.entities.UserEntity;
import com.spring.emprendedoresApp.persistence.repositories.RoleRepository;
import com.spring.emprendedoresApp.persistence.repositories.UserRepository;
import com.spring.emprendedoresApp.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;
	
    @Autowired
    private UserValidation userValidation;

	@Override
	public ResponseDTO createUser(UserEntity user, String roleName) throws Exception {
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

	// Método para obtener todos los usuarios
	@Override
	public List<UserEntity> getAllUsers() {
		return userRepository.findAll();
	}

	// Método para obtener un usuario por su ID
	@Override
	public Optional<UserEntity> getUserById(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
		}
		return userRepository.findById(id);
	}

	// Método para actualizar un usuario completamente (PUT)
	@Override
	public UserEntity updateUser(Long id, UserEntity updatedUser) {
		if (id == null || updatedUser == null) {
			throw new IllegalArgumentException("El ID del usuario y los datos a actualizar no pueden ser nulos");
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

		UserEntity existingUser = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("El usuario con el ID especificado no existe"));

		// Actualizar los campos específicos si no son nulos
		if (updatedUser.getUsername() != null) {
			existingUser.setUsername(updatedUser.getUsername());
		}
		if (updatedUser.getEmail() != null) {
			existingUser.setEmail(updatedUser.getEmail());
		}
		if (updatedUser.getPassword() != null) {
			existingUser.setPassword(encoder.encode(updatedUser.getPassword())); // Encriptar la nueva contraseña
		}
		if (updatedUser.getPhone() != null) {
			existingUser.setPhone(updatedUser.getPhone());
		}
		if (updatedUser.getCity() != null) {
			existingUser.setCity(updatedUser.getCity());
		}
		if (updatedUser.getCountry() != null) {
			existingUser.setCountry(updatedUser.getCountry());
		}

		// Agregar lógica para cambiar el tipo de usuario solo en el PUT (si es
		// necesario)
		if (updatedUser.getUserType() != null) {
			existingUser.setUserType(updatedUser.getUserType());
		}

		// Guardar el usuario actualizado
		return userRepository.save(existingUser);
	}

	// Método para actualizar parcialmente un usuario (PATCH)
	@Override
	public UserEntity updatePartialUser(Long id, UserEntity updatedUser) {
		if (id == null || updatedUser == null) {
			throw new IllegalArgumentException("El ID del usuario y los datos a actualizar no pueden ser nulos");
		}
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

		UserEntity existingUser = userRepository.findById(id)
				.orElseThrow(() -> new IllegalArgumentException("El usuario con el ID especificado no existe"));

		// Actualizar solo los campos no nulos
		if (updatedUser.getUsername() != null) {
			existingUser.setUsername(updatedUser.getUsername());
		}
		if (updatedUser.getEmail() != null) {
			existingUser.setEmail(updatedUser.getEmail());
		}
		if (updatedUser.getPassword() != null) {
			existingUser.setPassword(encoder.encode(updatedUser.getPassword())); // Encriptar la nueva contraseña
		}
		if (updatedUser.getPhone() != null) {
			existingUser.setPhone(updatedUser.getPhone());
		}
		if (updatedUser.getCity() != null) {
			existingUser.setCity(updatedUser.getCity());
		}
		if (updatedUser.getCountry() != null) {
			existingUser.setCountry(updatedUser.getCountry());
		}

		// No se permite cambiar el tipo de usuario en el PATCH
		// No se realiza ninguna modificación en el userType en el método PATCH

		// Guardar el usuario actualizado
		return userRepository.save(existingUser);
	}

	// Método para eliminar un usuario
	@Override
	public boolean deleteUser(Long id) {
		if (id == null) {
			throw new IllegalArgumentException("El ID del usuario no puede ser nulo");
		}

		if (userRepository.existsById(id)) {
			userRepository.deleteById(id);
			return true;
		} else {
			throw new IllegalArgumentException("El usuario con el ID especificado no existe");
		}
	}

	// Método para obtener usuarios paginados
	@Transactional(readOnly = true)
	@Override
	public Page<UserEntity> findAll(Pageable pageable) {
		return userRepository.findAll(pageable);
	}

}
