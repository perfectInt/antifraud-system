package io.sultanov.antifraudsystem.domain.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLockRequest {
    private String username;
    private String operation;
}
