package org.proj.chatapisocket.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.proj.chatapisocket.dto.CheckAuth;
import org.proj.chatapisocket.dto.JwtAuthenticationResponse;
import org.proj.chatapisocket.dto.SignInRequest;
import org.proj.chatapisocket.dto.SignUpRequest;
import org.proj.chatapisocket.security.jwt.JwtUtil;
import org.proj.chatapisocket.services.AuthenticationService;
import org.proj.chatapisocket.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@Tag(name = "Аутентификация")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationService authenticationService, JwtUtil jwtUtil, UserService userService) {
        this.authenticationService = authenticationService;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }
    @PostMapping("/check-auth")
    public Boolean checkAuth(@RequestBody CheckAuth auth){
        UserDetails userDetails = userService
                .userDetailsService()
                .loadUserByUsername(auth.getUsername());
        return jwtUtil.isTokenValid(auth.getToken(), userDetails);
    }
    @PostMapping("/regenerate-token")
    public ResponseEntity<?> regenerateToken(@RequestBody CheckAuth auth){
        try {
            UserDetails userDetails = userService
                    .userDetailsService()
                    .loadUserByUsername(auth.getUsername());
            if (jwtUtil.isTokenValid(auth.getToken(), userDetails)) {
                String token = jwtUtil.generateToken(userDetails);
                return ResponseEntity.ok(new JwtAuthenticationResponse(token));
            } else {
                return ResponseEntity.badRequest().body("token was already expired");
            }
        }catch (Exception e){
            return ResponseEntity.badRequest().body("there was a problem with username or token");
        }
    }
}
