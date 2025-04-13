package org.proj.chatapisocket.controllers;

import org.proj.chatapisocket.dto.UserDto;
import org.proj.chatapisocket.models.User;
import org.proj.chatapisocket.repos.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserDto> getUsers(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(required = false) String username
    ) {
        Long currentUserId = currentUser.getId();

        return userRepository.findAll().stream()
                .filter(user -> !user.getId().equals(currentUserId))
                .filter(user -> username == null || user.getUsername().toLowerCase().contains(username.toLowerCase()))
                .map(user -> new UserDto(user.getId(), user.getUsername()))
                .collect(Collectors.toList());
    }

}
