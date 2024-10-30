package org.zakariafarih.recipemakerbackend;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.zakariafarih.recipemakerbackend.entity.Role;
import org.zakariafarih.recipemakerbackend.repository.RoleRepository;
import org.zakariafarih.recipemakerbackend.service.IdGeneratorService;

@SpringBootApplication
public class RecipeMakerBackEndApplication {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private IdGeneratorService idGeneratorService;

    public static void main(String[] args) {
        SpringApplication.run(RecipeMakerBackEndApplication.class, args);
    }

    @PostConstruct
    public void initRoles() throws Exception {
        if (!roleRepository.findByName("ROLE_USER").isPresent()) {
            Role userRole = new Role();
            userRole.setId(idGeneratorService.generateRoleId());
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        }

        if (!roleRepository.findByName("ROLE_ADMIN").isPresent()) {
            Role adminRole = new Role();
            adminRole.setId(idGeneratorService.generateRoleId());
            adminRole.setName("ROLE_ADMIN");
            roleRepository.save(adminRole);
        }
    }
}

