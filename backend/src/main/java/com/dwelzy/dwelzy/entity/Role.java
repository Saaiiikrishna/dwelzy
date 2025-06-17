package com.dwelzy.dwelzy.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false, unique = true)
    private RoleName name;

    @Size(max = 255)
    @Column(name = "description")
    private String description;

    // Constructors
    public Role() {}

    public Role(RoleName name) {
        this.name = name;
    }

    public Role(RoleName name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public RoleName getName() {
        return name;
    }

    public void setName(RoleName name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Enum for role names
    public enum RoleName {
        ROLE_USER("Regular user with basic permissions"),
        ROLE_DRIVER("Driver with delivery permissions"),
        ROLE_ADMIN("Administrator with full permissions"),
        ROLE_HUB_MANAGER("Hub manager with hub-specific permissions");

        private final String description;

        RoleName(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }
}
