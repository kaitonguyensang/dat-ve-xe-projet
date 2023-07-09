package com.example.datvexe.services.impl;

import com.example.datvexe.common.Role;
import com.example.datvexe.models.TaiKhoan;
import com.example.datvexe.payloads.requests.SignUpRequest;
import com.example.datvexe.repositories.AdminRepository;
import com.example.datvexe.repositories.NhaXeRepository;
import com.example.datvexe.repositories.TaiKhoanRepository;
import com.example.datvexe.repositories.UserRepository;
import com.example.datvexe.services.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NhaXeRepository nhaXeRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public int checkInForUpdateAccount(SignUpRequest signUpRequest, TaiKhoan taiKhoan){
        TaiKhoan taiKhoanCheck = taiKhoanRepository.findTaiKhoanByAdmin_Sdt(signUpRequest.getSdt());
        if(taiKhoanCheck != taiKhoan && taiKhoanCheck != null) return 1;
        taiKhoanCheck = taiKhoanRepository.findTaiKhoanByNhaXe_Sdt(signUpRequest.getSdt());
        if(taiKhoanCheck != taiKhoan && taiKhoanCheck != null) return 1;
        taiKhoanCheck = taiKhoanRepository.findTaiKhoanByUser_Sdt(signUpRequest.getSdt());
        if(taiKhoanCheck != taiKhoan && taiKhoanCheck != null) return 1;
        if (signUpRequest.getRole()== Role.NHAXE){
            taiKhoanCheck = taiKhoanRepository.findTaiKhoanByNhaXe_TenNhaXe(signUpRequest.getTenNhaXe());
            if(taiKhoanCheck != taiKhoan && taiKhoanCheck != null) return 2;
            return 5;
        }
        taiKhoanCheck = taiKhoanRepository.findTaiKhoanByNhaXe_Email(signUpRequest.getEmail());
        if(taiKhoanCheck != taiKhoan && taiKhoanCheck != null) return 3;
        taiKhoanCheck = taiKhoanRepository.findTaiKhoanByAdmin_Email(signUpRequest.getEmail());
        if(taiKhoanCheck != taiKhoan && taiKhoanCheck != null) return 3;
        taiKhoanCheck = taiKhoanRepository.findTaiKhoanByUser_Email(signUpRequest.getEmail());
        if(taiKhoanCheck != taiKhoan && taiKhoanCheck != null) return 3;
        taiKhoanCheck = taiKhoanRepository.findTaiKhoanByUser_Cmnd(signUpRequest.getCmnd());
        if(taiKhoanCheck != taiKhoan && taiKhoanCheck != null) return 4;
        taiKhoanCheck = taiKhoanRepository.findTaiKhoanByAdmin_Cmnd(signUpRequest.getCmnd());
        if(taiKhoanCheck != taiKhoan && taiKhoanCheck != null) return 4;
        return 5;
    }

    @Override
    public String changePasswordToPasswordEncode(String password) {
        return passwordEncoder.encode(password);
    }
}
