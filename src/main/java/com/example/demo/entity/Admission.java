package com.example.demo.entity;

import com.example.demo.common.entity.BaseEntity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "admissions")
public class Admission extends BaseEntity {
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
