package com.example.demo.entity;

import com.example.authorizationValidator.entity.WithPrimaryKeyBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;

import java.util.Set;

@Entity
@Table(name = "admissions")
public class Admission extends WithPrimaryKeyBaseEntity {
    private String username;
    private String password;
    private Set<Role> roles;

    @Column(unique = true, nullable = false)
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Column(nullable = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ElementCollection(targetClass = Role.class)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}
