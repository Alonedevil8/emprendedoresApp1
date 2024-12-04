package com.spring.emprendedoresApp.services;

import com.spring.emprendedoresApp.models.dtos.ResponseDTO;
import com.spring.emprendedoresApp.persistence.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IUserService {

    // Crear un nuevo usuario
    ResponseDTO createUser(UserEntity user, String roleName) throws Exception;

    // Obtener todos los usuarios
    List<UserEntity> getAllUsers();

    // Obtener un usuario por su ID
    Optional<UserEntity> getUserById(Long id);

    // Actualizar un usuario existente
    UserEntity updateUser(Long id, UserEntity updatedUser);

    // Eliminar un usuario
    boolean deleteUser(Long id);

    // Obtener usuarios con paginación
    Page<UserEntity> findAll(Pageable pageable);

    // Actualizar parcialmente un usuario
    UserEntity updatePartialUser(Long id, UserEntity partialUser);
}
