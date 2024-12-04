package com.spring.emprendedoresApp.services.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.emprendedoresApp.persistence.entities.UserEntity;
import com.spring.emprendedoresApp.persistence.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository repository;

    // Sobrescribimos el método para cargar un usuario por su nombre de usuario.
    // Este método es llamado automáticamente por Spring Security durante el proceso
    // de autenticación.
    @Override
    @Transactional(readOnly = true) // Indicamos que esta operación es de solo lectura para optimizar el rendimiento
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Buscamos el usuario en la base de datos por su nombre de usuario.
        // Optional permite manejar el caso en el que el usuario no se encuentre.
        Optional<UserEntity> optionalUser = repository.findByUsername(username);

        // Si el usuario no existe, lanzamos una excepción indicando que no se encontró.
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException(String.format("Username %s no existe en el sistema", username));
        }

        // Extraemos el usuario del Optional. Aquí es seguro ya que se ha verificado que
        // no esté vacío.
        UserEntity user = optionalUser.orElseThrow();

        // Mapear roles a SimpleGrantedAuthority
        List<GrantedAuthority> authorities = List.of(
        	    new SimpleGrantedAuthority("ROLE_" + user.getRole().getRoleName().name())
        	);

        // Creamos una instancia de User de Spring Security con los datos del usuario,
        // incluyendo nombre de usuario, contraseña y los permisos (roles).
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), true, true, true,
                true, authorities);
    }
}
