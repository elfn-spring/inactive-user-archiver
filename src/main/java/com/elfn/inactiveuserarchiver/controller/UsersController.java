package com.elfn.inactiveuserarchiver.controller;

import com.elfn.inactiveuserarchiver.dao.ArchivedUserRepository;
import com.elfn.inactiveuserarchiver.dao.UserRepository;
import com.elfn.inactiveuserarchiver.model.ArchivedUser;
import com.elfn.inactiveuserarchiver.model.Users;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Elimane
 */
@RestController
@RequestMapping("/api")
public class UsersController {
    private final UserRepository userRepository;
    private final ArchivedUserRepository archivedUserRepository;

    public UsersController(UserRepository userRepository, ArchivedUserRepository archivedUserRepository) {
        this.userRepository = userRepository;
        this.archivedUserRepository = archivedUserRepository;
    }

    @GetMapping("/users")
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/archived")
    public List<ArchivedUser> getAllArchivedUsers() {
        return archivedUserRepository.findAll();
    }
}
