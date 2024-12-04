package com.spring.emprendedoresApp.persistence.repositories;

import com.spring.emprendedoresApp.persistence.entities.RoleEntity;
import com.spring.emprendedoresApp.persistence.entities.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Optional<UserEntity> findByEmail(String email);
    
    // Método para obtener todos los usuarios en un formato paginado
    Page<UserEntity> findAll(Pageable pageable);
    
    // Método para encontrar un usuario por su nombre de usuario (username)
    Optional<UserEntity> findByUsername(String name);
    
    // Método para obtener una lista de usuarios basados en el nombre del rol (roleName)
    List<UserEntity> findByRole_RoleName(RoleEntity.RoleName roleName);
}
