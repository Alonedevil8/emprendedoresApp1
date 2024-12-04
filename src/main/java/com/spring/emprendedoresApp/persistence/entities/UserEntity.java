package com.spring.emprendedoresApp.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotNull(message = "El nombre de usuario no puede ser nulo")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    @Column(name = "username")
    private String username;

    @NotNull(message = "El correo no puede ser nulo")
    @Email(message = "El correo debe ser válido")
    @Column(name = "email", unique = true)
    private String email;

    @NotNull(message = "La contraseña no puede ser nula")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    @Column(name = "password")
    private String password;

    @NotNull(message = "El teléfono no puede ser nulo")
    @Size(min = 1, max = 255, message = "El teléfono debe tener entre 1 y 255 caracteres")
    @Column(name = "phone")
    private String phone;

    @NotNull(message = "La ciudad no puede ser nula")
    @Size(min = 1, max = 255, message = "La ciudad debe tener entre 1 y 255 caracteres")
    @Column(name = "city")
    private String city;

    @NotNull(message = "El país no puede ser nulo")
    @Size(min = 1, max = 255, message = "El país debe tener entre 1 y 255 caracteres")
    @Column(name = "country")
    private String country;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonIgnore  // Evita serializar esta propiedad en las respuestas JSON
    private RoleEntity role;

    @Column(name = "registration_date", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime registrationDate;

    // Cambia el tipo a String (sin valor por defecto)
    @Column(name = "user_type")
    private String userTyp;

    // Relación OneToMany para los comentarios
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @JsonIgnore
    private Set<CommentEntity> comments;

    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = new RoleEntity();
            this.role.setRoleName(RoleEntity.RoleName.ROLE_USER); // Rol por defecto
        }
        if (this.registrationDate == null) {
            this.registrationDate = LocalDateTime.now();
        }
        if (this.userTyp == null) {
            this.userTyp = "Otro"; // Valor por defecto si es null
        }
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public RoleEntity getRole() {
        return role;
    }

    public void setRole(RoleEntity role) {
        this.role = role;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getUserType() {
        return userTyp;
    }

    public void setUserType(String userType) {
        this.userTyp = userType;
    }

    public Set<CommentEntity> getComments() {
        return comments;
    }

    public void setComments(Set<CommentEntity> comments) {
        this.comments = comments;
    }
}
