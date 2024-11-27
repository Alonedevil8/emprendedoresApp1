package com.spring.emprendedoresApp.services;

import com.spring.emprendedoresApp.persistence.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    UserEntity createUser(UserEntity user, String roleName);
    List<UserEntity> getAllUsers();
    Optional<UserEntity> getUserById(Long id);
    UserEntity updateUser(Long id, UserEntity updatedUser);
    boolean deleteUser(Long id);
    List<UserEntity> getUsersByRole(String roleName);
}
