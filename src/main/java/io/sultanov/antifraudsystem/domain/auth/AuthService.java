package io.sultanov.antifraudsystem.domain.auth;

import io.sultanov.antifraudsystem.domain.user.*;
import io.sultanov.antifraudsystem.security.config.JwtService;
import io.sultanov.antifraudsystem.utils.Operation;
import io.sultanov.antifraudsystem.utils.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public User registerUser(RegisterDto registerDto) {
        if (userRepository.findUserByUsername(registerDto.getUsername()).isPresent())
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        User user = userMapper.toEntity(registerDto);
        user.setPassword(encoder.encode(registerDto.getPassword()));
        user.setRole(Role.MERCHANT);
        if (userRepository.findAll().isEmpty()) {
            user.setRole(Role.ADMINISTRATOR);
            user.setNonLocked(true);
        }
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public DeleteUserResponse deleteUserByUsername(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.getRole() == Role.ADMINISTRATOR) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        userRepository.deleteByUsername(username);
        return new DeleteUserResponse(username, "Deleted successfully!");
    }

    public User changeUserRole(ChangeUserRoleRequest request) {
        User user = userRepository.findUserByUsername(request.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (Objects.equals(request.getRole(), Role.ADMINISTRATOR.name()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        if (Objects.equals(user.getRole().name(), request.getRole()))
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        user.setRole(Role.valueOf(request.getRole()));
        return userRepository.save(user);
    }

    public String login(LoginDto loginDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getUsername(),
                        loginDto.getPassword()
                )
        );
        var user = userRepository.findUserByUsername(loginDto.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return jwtService.generateToken(user);
    }

    public UserLockResponse changerUserAccess(UserLockRequest request) {
        User user = userRepository.findUserByUsername(request.getUsername()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (user.getRole() == Role.ADMINISTRATOR) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        user.setNonLocked(!Objects.equals(request.getOperation(), Operation.LOCK.name()));
        userRepository.save(user);
        return new UserLockResponse("User " + user.getUsername() + " was " + (Objects.equals(request.getOperation(), "LOCK") ? "locked" : "unlocked"));
    }
}
