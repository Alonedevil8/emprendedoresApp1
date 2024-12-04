package com.spring.emprendedoresApp.services.impl;

import com.spring.emprendedoresApp.persistence.entities.RoleEntity;
import com.spring.emprendedoresApp.persistence.entities.UserEntity;
import com.spring.emprendedoresApp.persistence.repositories.UserRepository;
import com.spring.emprendedoresApp.services.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    private UserRepository userRepository;

    // Método para obtener usuarios por su rol
    @Override
    public List<UserEntity> getUsersByRole(String roleName) {
        // Convertir el String recibido como parámetro en un valor de tipo RoleName (Enum)
        RoleEntity.RoleName roleEnum;
        try {
            // Intentamos convertir el String al Enum correspondiente
            roleEnum = RoleEntity.RoleName.valueOf(roleName.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Si el String no corresponde a un valor válido en el Enum, lanzamos una excepción
            throw new IllegalArgumentException("Invalid role name: " + roleName);
        }

        // Llamamos al repositorio para buscar los usuarios que tienen el rol especificado
        return userRepository.findByRole_RoleName(roleEnum);
    }
}
