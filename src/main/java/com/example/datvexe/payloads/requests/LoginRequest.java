package com.example.datvexe.payloads.requests;

import com.example.datvexe.common.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class LoginRequest {
    private String username;
    private String password;
    private String role;
}
