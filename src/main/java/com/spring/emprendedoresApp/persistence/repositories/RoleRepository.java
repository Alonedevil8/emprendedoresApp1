package com.spring.emprendedoresApp.persistence.repositories;

import com.spring.emprendedoresApp.persistence.entities.RoleEntity;
import com.spring.emprendedoresApp.persistence.entities.RoleEntity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    // Método para obtener un RoleEntity basado en su nombre de rol (RoleName)
    RoleEntity findByRoleName(RoleName roleName);
}
