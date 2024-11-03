package org.zakariafarih.recipemakerbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zakariafarih.recipemakerbackend.repository.RoleRepository;
import org.zakariafarih.recipemakerbackend.repository.UserRepository;

@Service
public class IdGeneratorService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public synchronized Long generateUserId() {
        Integer maxId = userRepository.findMaxId();
        return (long) ((maxId != null ? maxId : 0) + 1);
    }

    public synchronized Long generateRoleId() {
        Integer maxId = roleRepository.findMaxId();
        return (long) ((maxId != null ? maxId : 0) + 1);
    }
}
