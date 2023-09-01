package io.sultanov.antifraudsystem.domain.user;

import io.sultanov.antifraudsystem.domain.auth.RegisterDto;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(RegisterDto registerDto) {
        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        return user;
    }
}
