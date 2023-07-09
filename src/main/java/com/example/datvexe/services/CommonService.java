package com.example.datvexe.services;

import com.example.datvexe.models.TaiKhoan;
import com.example.datvexe.payloads.requests.SignUpRequest;
import org.springframework.stereotype.Component;

@Component
public interface CommonService {
    int checkInForUpdateAccount(SignUpRequest signUpRequest, TaiKhoan taiKhoan);

    String changePasswordToPasswordEncode(String password);
}
