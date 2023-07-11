package com.example.datvexe.services.impl;

import com.example.datvexe.common.Role;
import com.example.datvexe.common.TrangThai;
import com.example.datvexe.models.Admin;
import com.example.datvexe.models.NhaXe;
import com.example.datvexe.models.TaiKhoan;
import com.example.datvexe.models.User;
import com.example.datvexe.payloads.requests.SignUpRequest;
import com.example.datvexe.repositories.AdminRepository;
import com.example.datvexe.repositories.NhaXeRepository;
import com.example.datvexe.repositories.TaiKhoanRepository;
import com.example.datvexe.repositories.UserRepository;
import com.example.datvexe.services.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class SignUpServiceImpl implements SignUpService {

    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NhaXeRepository nhaXeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public TaiKhoan convertSignUpToTaiKhoan(SignUpRequest signUpRequest){
        TaiKhoan taiKhoan = new TaiKhoan();
        taiKhoan.setUsername(signUpRequest.getUsername());
        taiKhoan.setPassword(signUpRequest.getPassword());
        taiKhoan.setRole(signUpRequest.getRole());
        taiKhoan.setTrangThaiHoatDong(TrangThai.ACTIVE);
        if (signUpRequest.getRole() == Role.NHAXE) taiKhoan.setTrangThaiHoatDong(TrangThai.INACTIVE);
        return taiKhoan;
    }

    public User convertSignUpToUser(SignUpRequest signUpRequest, TaiKhoan taiKhoan) {
        User user = new User();
        user.setHoTen(signUpRequest.getHoTen());
        user.setCmnd(signUpRequest.getCmnd());
        user.setSdt(signUpRequest.getSdt());
        user.setEmail(signUpRequest.getEmail());
        user.setDiaChi(signUpRequest.getDiaChi());
        user.setTaiKhoan(taiKhoan);
        user.setPicture(signUpRequest.getPicture());
        return user;
    }

    public NhaXe convertSignUpToNhaXe(SignUpRequest signUpRequest, TaiKhoan taiKhoan){
        NhaXe nhaXe = new NhaXe();
        nhaXe.setTenNhaXe(signUpRequest.getTenNhaXe());
        nhaXe.setSdt(signUpRequest.getSdt());
        nhaXe.setMoTaNgan(signUpRequest.getMoTaNgan());
        nhaXe.setEmail(signUpRequest.getEmail());
        nhaXe.setDiaChi(signUpRequest.getDiaChi());
        nhaXe.setTaiKhoan(taiKhoan);
        nhaXe.setPicture(signUpRequest.getPicture());
        return nhaXe;
    }

    public Admin convertSignUpToAdmin(SignUpRequest signUpRequest, TaiKhoan taiKhoan){
        Admin admin = new Admin();
        admin.setName(signUpRequest.getName());
        admin.setEmail(signUpRequest.getEmail());
        admin.setSdt(signUpRequest.getSdt());
        admin.setCmnd(signUpRequest.getCmnd());
        admin.setTaiKhoan(taiKhoan);
        return admin;
    }

    public int checkInfo(SignUpRequest signUpRequest){
        TaiKhoan taiKhoan = taiKhoanRepository.findTaiKhoanByUsername(signUpRequest.getUsername());
        if(taiKhoan != null) return 1;

        //User
        User userold = userRepository.findUserByCmnd(signUpRequest.getCmnd());
        if(userold != null) return 2;
        userold = userRepository.findUserBySdt(signUpRequest.getSdt());
        if(userold != null) return 3;
        userold = userRepository.findUserByEmail(signUpRequest.getEmail());
        if(userold != null) return 4;

        //NhaXe
        NhaXe nhaXe = nhaXeRepository.findNhaXeBySdt(signUpRequest.getSdt());
        if(nhaXe != null) return 3;
        nhaXe = nhaXeRepository.findNhaXesByEmail(signUpRequest.getEmail());
        if(nhaXe != null) return 4;

        //Admin
        Admin admin = adminRepository.findAdminByCmnd(signUpRequest.getCmnd());
        if(admin != null) return 2;
        admin = adminRepository.findAdminBySdt(signUpRequest.getSdt());
        if(admin != null) return 3;
        admin = adminRepository.findAdminByEmail(signUpRequest.getEmail());
        if(admin != null) return 4;
        return 5;
    }

    public int addTaiKhoanUser(SignUpRequest signUpRequest){

        int check = checkInfo(signUpRequest);
        if (check!=5) return check;

        TaiKhoan taiKhoanNew = convertSignUpToTaiKhoan(signUpRequest);
        String password = passwordEncoder.encode(signUpRequest.getPassword());
        taiKhoanNew.setPassword(password);
        TaiKhoan taiKhoanAdd = taiKhoanRepository.save(taiKhoanNew);
        TaiKhoan taiKhoanOfUsername = new TaiKhoan();
        if(taiKhoanAdd != null)  {
            taiKhoanOfUsername = taiKhoanRepository.findTaiKhoanByUsername(taiKhoanAdd.getUsername());
        }
        User userNew = convertSignUpToUser(signUpRequest, taiKhoanOfUsername);
        User userAdd = userRepository.save(userNew);
        if (userAdd != null) return 5;
        else return 6;
    }

    public int addTaiKhoanNhaXe(SignUpRequest signUpRequest){

        int check = checkInfo(signUpRequest);
        if (check!=5) return check;

        NhaXe nhaXe = nhaXeRepository.findNhaXeByTenNhaXe(signUpRequest.getTenNhaXe());
        if (nhaXe != null) return 6;
        TaiKhoan taiKhoanNew= convertSignUpToTaiKhoan(signUpRequest);
        String password = passwordEncoder.encode(signUpRequest.getPassword());
        taiKhoanNew.setPassword(password);
        TaiKhoan taiKhoanAdd = taiKhoanRepository.save(taiKhoanNew);
        NhaXe nhaXeNew = convertSignUpToNhaXe(signUpRequest,taiKhoanAdd);
        nhaXeRepository.save(nhaXeNew);
        return 5;
    }
    public int addTaiKhoanAdmin(SignUpRequest signUpRequest) {

        int check = checkInfo(signUpRequest);
        if (check != 5) return check;

        TaiKhoan taiKhoanNew = convertSignUpToTaiKhoan(signUpRequest);
        String password = passwordEncoder.encode(signUpRequest.getPassword());
        taiKhoanNew.setPassword(password);
        TaiKhoan taiKhoanAdd = taiKhoanRepository.save(taiKhoanNew);
        TaiKhoan taiKhoanOfAdmin = new TaiKhoan();
        if (taiKhoanAdd != null) {
            taiKhoanOfAdmin = taiKhoanRepository.findTaiKhoanByUsername(taiKhoanAdd.getUsername());
        }
        Admin adminnew = convertSignUpToAdmin(signUpRequest, taiKhoanOfAdmin);
        Admin adminAdd = adminRepository.save(adminnew);
        if (adminAdd != null) return 5;
        else return 6;
    }


}
