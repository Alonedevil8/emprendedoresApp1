package com.spring.emprendedoresApp.persistence.repositories;

import com.spring.emprendedoresApp.persistence.entities.UserEntity;
import com.spring.emprendedoresApp.persistence.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // Método para obtener una lista de usuarios basados en el nombre del rol
    List<UserEntity> findByRole_RoleName(RoleEntity.RoleName roleName);
}
