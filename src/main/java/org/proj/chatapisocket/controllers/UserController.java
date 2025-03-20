package org.proj.chatapisocket.controllers;

import org.proj.chatapisocket.models.User;
import org.proj.chatapisocket.repos.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @GetMapping()
    public List<User> getUsers() {
        List<User> users = new ArrayList<User>();
        users = userRepository.findAll();
        return users;
    }
}
