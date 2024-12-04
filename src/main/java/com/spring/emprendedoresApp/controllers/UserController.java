package com.spring.emprendedoresApp.controllers;

import com.spring.emprendedoresApp.models.dtos.ResponseDTO;
import com.spring.emprendedoresApp.persistence.entities.UserEntity;
import com.spring.emprendedoresApp.services.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users") // Ruta base para todas las solicitudes relacionadas con usuarios
public class UserController {

    @Autowired
    private IUserService userService; // Inyección del servicio de usuarios
    
    // Endpoint para obtener el usuario actual y sus roles
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Extrae información del usuario
        String username = authentication.getName(); // Nombre de usuario
        var authorities = authentication.getAuthorities().stream()
                .map(Object::toString)
                .collect(Collectors.toList()); // Lista de roles

        // Devuelve los datos como respuesta
        return ResponseEntity.ok(Map.of(
                "username", username,
                "roles", authorities
        ));
    }

    // Endpoint para registrar un nuevo usuario
    @PostMapping("/register/{roleName}")
    private ResponseEntity<ResponseDTO> register(@RequestBody UserEntity user, @PathVariable String roleName) throws Exception {
        return new ResponseEntity<>(userService.createUser(user, roleName), HttpStatus.CREATED);
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
        return user.isPresent() ? new ResponseEntity<>(user.get(), HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Endpoint para actualizar un usuario por su ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @Valid @RequestBody UserEntity updatedUser,
            BindingResult result) {
        if (result.hasErrors()) {
            // Si hay errores de validación, se devuelven los errores al cliente
            StringBuilder errorMessage = new StringBuilder("Errores de validación: ");
            for (ObjectError error : result.getAllErrors()) {
                errorMessage.append(error.getDefaultMessage()).append("; ");
            }
            return new ResponseEntity<>(errorMessage.toString(), HttpStatus.BAD_REQUEST);
        }

        // Llamada al servicio para actualizar el usuario
        UserEntity updated = userService.updateUser(id, updatedUser);
        // Si el usuario fue actualizado, retorna el usuario con el código de estado 200
        // (OK)
        // Si no se encontró el usuario, retorna el código de estado 404 (NOT FOUND)
        return updated != null ? new ResponseEntity<>(updated, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
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

    // Endpoint para obtener una lista paginada de usuarios
    @GetMapping("/page/{page}")
    public Page<UserEntity> listPageable(@PathVariable Integer page) {
        Pageable pageable = PageRequest.of(page, 5); // Paginación con 5 usuarios por página
        return userService.findAll(pageable); // Retorna la página solicitada de usuarios
    }
    
    // Endpoint para actualizar parcialmente un usuario por su ID (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePartialUser(@PathVariable Long id, @RequestBody UserEntity partialUser) {
        // Verifica si el usuario existe
        Optional<UserEntity> userOptional = userService.getUserById(id);
        
        if (!userOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // Llamada al servicio para actualizar parcialmente el usuario
        UserEntity updatedUser = userService.updatePartialUser(id, partialUser);

        // Retorna el usuario actualizado con el código de estado 200 (OK)
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
