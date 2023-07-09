package com.example.datvexe.services;

import com.example.datvexe.models.Admin;
import com.example.datvexe.models.NhaXe;
import com.example.datvexe.models.TaiKhoan;
import com.example.datvexe.models.User;
import com.example.datvexe.payloads.requests.SignUpRequest;
import org.springframework.stereotype.Component;

@Component
public interface SignUpService {

    int addTaiKhoanUser(SignUpRequest signUpRequest);
    int addTaiKhoanNhaXe(SignUpRequest signUpRequest);
    int addTaiKhoanAdmin(SignUpRequest signUpRequest);
    int checkInfo(SignUpRequest signUpRequest);
    Admin convertSignUpToAdmin(SignUpRequest signUpRequest, TaiKhoan taiKhoan);
    NhaXe convertSignUpToNhaXe(SignUpRequest signUpRequest, TaiKhoan taiKhoan);
    User convertSignUpToUser(SignUpRequest signUpRequest, TaiKhoan taiKhoan);
    TaiKhoan convertSignUpToTaiKhoan(SignUpRequest signUpRequest);

}
