package io.sultanov.antifraudsystem.api.controllers;

import io.sultanov.antifraudsystem.domain.auth.AuthService;
import io.sultanov.antifraudsystem.domain.auth.LoginDto;
import io.sultanov.antifraudsystem.domain.auth.RegisterDto;
import io.sultanov.antifraudsystem.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@RequestBody RegisterDto registerDto) {
        return authService.registerUser(registerDto);
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyAuthority('ADMINISTRATOR', 'SUPPORT')")
    public List<User> getAllUsers() {
        return authService.getAllUsers();
    }

    @DeleteMapping("/user/{username}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public DeleteUserResponse deleteUser(@PathVariable String username) {
        return authService.deleteUserByUsername(username);
    }

    @PutMapping("/role")
    @ResponseStatus(HttpStatus.OK)
    public User changeUserRole(@RequestBody ChangeUserRoleRequest request) {
        return authService.changeUserRole(request);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public String login(@RequestBody LoginDto loginDto) {
        return authService.login(loginDto);
    }

    @PutMapping("/access")
    @ResponseStatus(HttpStatus.OK)
    public UserLockResponse changeUserAccess(@RequestBody UserLockRequest request) {
        return authService.changerUserAccess(request);
    }
}
