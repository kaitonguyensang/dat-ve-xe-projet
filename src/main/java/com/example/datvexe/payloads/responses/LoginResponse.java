package com.example.datvexe.payloads.responses;

import com.example.datvexe.common.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LoginResponse {
    private String jwtToken;
    private Long id;
    private String username;
    private Role role;
    private String email;

    public LoginResponse(String jwtToken, Role role, Long id, String username, String email) {
        this.jwtToken = jwtToken;
        this.id = id;
        this.role = role;
        this.username = username;
        this.email = email;
    }
}
