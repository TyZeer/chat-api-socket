package org.proj.chatapisocket.controllers;

import org.proj.chatapisocket.dto.UserDto;
import org.proj.chatapisocket.models.User;
import org.proj.chatapisocket.repos.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @GetMapping()
    public List<UserDto> getUsers(@AuthenticationPrincipal User currentUser) {
        Long userId = currentUser.getId();
        return userRepository.findAll().stream()
                .filter(u -> !u.getId().equals(userId))
                .map(u -> new UserDto(u.getId(), u.getUsername()))
                .collect(Collectors.toList());
    }
}
