package com.spring.emprendedoresApp.persistence.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "roles")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Column(name = "role_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    // Relación OneToMany con usuarios
    @OneToMany(mappedBy = "role")
    @JsonIgnore  // Evita la serialización de la lista de usuarios para evitar recursión
    private Set<UserEntity> users;

    // Enum para los nombres de roles
    public enum RoleName {
        ROLE_USER,
        ROLE_ADMIN,
        ROLE_EDITOR
    }

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }
}
