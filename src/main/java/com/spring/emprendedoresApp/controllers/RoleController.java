package com.spring.emprendedoresApp.controllers;

import com.spring.emprendedoresApp.persistence.entities.UserEntity;
import com.spring.emprendedoresApp.services.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")  // Ruta base para todas las solicitudes relacionadas con roles
public class RoleController {

    @Autowired
    private IRoleService roleService;  // Inyección del servicio de roles

    // Endpoint para obtener los usuarios por un rol específico
    @GetMapping("/{roleName}")  // Ruta para obtener los usuarios de un rol específico por su nombre
    public ResponseEntity<List<UserEntity>> getUsersByRole(@PathVariable String roleName) {
        // Llamada al servicio para obtener los usuarios asociados al rol
        List<UserEntity> users = roleService.getUsersByRole(roleName);
        // Retorna la lista de usuarios con el código de estado 200 (OK)
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
