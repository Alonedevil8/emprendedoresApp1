package com.spring.emprendedoresApp.services;

import com.spring.emprendedoresApp.persistence.entities.UserEntity;

import java.util.List;

public interface IRoleService {
    List<UserEntity> getUsersByRole(String roleName);
}
