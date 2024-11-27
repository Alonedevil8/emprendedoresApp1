package com.spring.emprendedoresApp.controllers;

import com.spring.emprendedoresApp.persistence.entities.UserEntity;
import com.spring.emprendedoresApp.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")  // Ruta base para todas las solicitudes relacionadas con usuarios
public class UserController {

    @Autowired
    private IUserService userService;  // Inyección del servicio de usuarios

    // Endpoint para crear un nuevo usuario con un rol específico
    @PostMapping("/{roleName}")  // Ruta para crear un usuario con un rol dado
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user, @PathVariable String roleName) {
        // Llamada al servicio para crear un nuevo usuario con el rol
        UserEntity createdUser = userService.createUser(user, roleName);
        // Retorna el usuario creado con el código de estado 201 (CREATED)
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Endpoint para obtener todos los usuarios
    @GetMapping
    public ResponseEntity<List<UserEntity>> getAllUsers() {
        // Llamada al servicio para obtener todos los usuarios
        List<UserEntity> users = userService.getAllUsers();
        // Retorna la lista de usuarios con el código de estado 200 (OK)
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Endpoint para obtener un usuario por su ID
    @GetMapping("/{id}")
    public ResponseEntity<UserEntity> getUserById(@PathVariable Long id) {
        // Llamada al servicio para obtener un usuario por su ID
        Optional<UserEntity> user = userService.getUserById(id);
        // Si el usuario existe, retorna el usuario con el código de estado 200 (OK)
        // Si no se encuentra, retorna el código de estado 404 (NOT FOUND)
        return user.isPresent() ? new ResponseEntity<>(user.get(), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para actualizar un usuario por su ID
    @PutMapping("/{id}")
    public ResponseEntity<UserEntity> updateUser(@PathVariable Long id, @RequestBody UserEntity updatedUser) {
        // Llamada al servicio para actualizar el usuario
        UserEntity updated = userService.updateUser(id, updatedUser);
        // Si el usuario fue actualizado, retorna el usuario con el código de estado 200 (OK)
        // Si no se encontró el usuario, retorna el código de estado 404 (NOT FOUND)
        return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para eliminar un usuario por su ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // Llamada al servicio para eliminar el usuario
        boolean isDeleted = userService.deleteUser(id);
        // Si el usuario fue eliminado, retorna el código de estado 204 (NO CONTENT)
        // Si no se encontró el usuario, retorna el código de estado 404 (NOT FOUND)
        return isDeleted ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para obtener los usuarios por su rol
    @GetMapping("/role/{roleName}")
    public ResponseEntity<List<UserEntity>> getUsersByRole(@PathVariable String roleName) {
        // Llamada al servicio para obtener los usuarios con el rol especificado
        List<UserEntity> users = userService.getUsersByRole(roleName);
        // Retorna la lista de usuarios con el código de estado 200 (OK)
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
