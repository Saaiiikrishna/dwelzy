package com.dwelzy.dwelzy.config;

import com.dwelzy.dwelzy.entity.Role;
import com.dwelzy.dwelzy.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize roles if they don't exist
        if (roleRepository.count() == 0) {
            createRoleIfNotExists(Role.RoleName.ROLE_USER, "Regular user with basic permissions");
            createRoleIfNotExists(Role.RoleName.ROLE_DRIVER, "Driver with delivery permissions");
            createRoleIfNotExists(Role.RoleName.ROLE_ADMIN, "Administrator with full permissions");
            createRoleIfNotExists(Role.RoleName.ROLE_HUB_MANAGER, "Hub manager with hub-specific permissions");
        }
    }

    private void createRoleIfNotExists(Role.RoleName roleName, String description) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = new Role(roleName, description);
            roleRepository.save(role);
            System.out.println("Created role: " + roleName);
        }
    }
}
