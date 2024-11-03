package org.zakariafarih.recipemakerbackend.service;

import org.zakariafarih.recipemakerbackend.dto.UserDTO;
import org.zakariafarih.recipemakerbackend.entity.Role;
import org.zakariafarih.recipemakerbackend.entity.User;
import org.zakariafarih.recipemakerbackend.repository.RoleRepository;
import org.zakariafarih.recipemakerbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private IdGeneratorService idGeneratorService;

    @Transactional
    public User registerNewUser(UserDTO userDTO) throws Exception {
        if (userRepository.existsByUsername(userDTO.getUsername())) {
            throw new Exception("Username is already taken!");
        }

        if (userDTO.getEmail() != null && userRepository.existsByEmail(userDTO.getEmail())) {
            throw new Exception("Email is already in use!");
        }

        User user = new User();
        // Remove manual ID assignment
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setEmail(userDTO.getEmail());

        // Assign default role
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new Exception("User Role not set."));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        return userRepository.save(user);
    }

    public Long getUserIdByUsername(String username) throws Exception {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("User not found"));
        return user.getId();
    }
}
