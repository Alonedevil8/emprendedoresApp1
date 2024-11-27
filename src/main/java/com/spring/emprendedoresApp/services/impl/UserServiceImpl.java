package com.spring.emprendedoresApp.services.impl;

import com.spring.emprendedoresApp.persistence.entities.RoleEntity;
import com.spring.emprendedoresApp.persistence.entities.UserEntity;
import com.spring.emprendedoresApp.persistence.repositories.UserRepository;
import com.spring.emprendedoresApp.persistence.repositories.RoleRepository;
import com.spring.emprendedoresApp.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    // Inyección de dependencias para los repositorios de usuarios y roles
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    // Método para crear un nuevo usuario
    @Override
    public UserEntity createUser(UserEntity user, String roleName) {
        // Convertir el String recibido como parámetro a un valor de tipo RoleName (Enum)
        RoleEntity.RoleName roleEnum = RoleEntity.RoleName.valueOf(roleName.toUpperCase());

        // Buscar el rol correspondiente en el repositorio y asignarlo al usuario
        user.setRole(roleRepository.findByRoleName(roleEnum));

        // Guardar el usuario en la base de datos
        return userRepository.save(user);
    }

    // Método para obtener todos los usuarios
    @Override
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();  // Devolver todos los usuarios del repositorio
    }

    // Método para obtener un usuario por su ID
    @Override
    public Optional<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);  // Buscar el usuario por ID
    }

    // Método para actualizar un usuario
    @Override
    public UserEntity updateUser(Long id, UserEntity updatedUser) {
        if (userRepository.existsById(id)) {
            UserEntity existingUser = userRepository.findById(id).orElse(null);

            if (existingUser != null) {
                // No sobrescribir el rol y la fecha de registro
                if (updatedUser.getUsername() != null) {
                    existingUser.setUsername(updatedUser.getUsername());  // Actualizar el nombre de usuario
                }
                if (updatedUser.getEmail() != null) {
                    existingUser.setEmail(updatedUser.getEmail());  // Actualizar el correo
                }
                if (updatedUser.getPassword() != null) {
                    existingUser.setPassword(updatedUser.getPassword());  // Actualizar la contraseña
                }
                if (updatedUser.getPhone() != null) {
                    existingUser.setPhone(updatedUser.getPhone());  // Actualizar el teléfono
                }
                if (updatedUser.getCity() != null) {
                    existingUser.setCity(updatedUser.getCity());  // Actualizar la ciudad
                }
                if (updatedUser.getCountry() != null) {
                    existingUser.setCountry(updatedUser.getCountry());  // Actualizar el país
                }
                // No se actualiza el rol ni la fecha de registro

                // Guardar el usuario con los campos actualizados
                return userRepository.save(existingUser);
            }
        }
        return null;  // Si el usuario no existe, retornar null
    }


    // Método para eliminar un usuario
    @Override
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);  // Eliminar el usuario por ID
            return true;  // Indicar que la eliminación fue exitosa
        }
        return false;  // Si el usuario no existe, retornar false
    }

    // Método para obtener usuarios por su rol
    @Override
    public List<UserEntity> getUsersByRole(String roleName) {
        // Convertir el String recibido como parámetro a un valor de tipo RoleName (Enum)
        RoleEntity.RoleName roleEnum = RoleEntity.RoleName.valueOf(roleName.toUpperCase());

        // Buscar y devolver los usuarios con el rol correspondiente
        return userRepository.findByRole_RoleName(roleEnum);  // Pasar el Enum al repositorio
    }
}
