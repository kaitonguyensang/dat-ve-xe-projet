package com.example.datvexe.services.impl;

import com.example.datvexe.common.TrangThai;
import com.example.datvexe.models.Admin;
import com.example.datvexe.models.TaiKhoan;
import com.example.datvexe.models.User;
import com.example.datvexe.payloads.requests.SignUpRequest;
import com.example.datvexe.payloads.requests.UserRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.repositories.TaiKhoanRepository;
import com.example.datvexe.repositories.UserRepository;
import com.example.datvexe.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    CommonServiceImpl commonService;

    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    private TaiKhoan convertUserRequestToTaiKhoan(UserRequest userRequest, Long userId) {
        TaiKhoan taiKhoan = taiKhoanRepository.findTaiKhoanByUser_Id(userId);
        if (taiKhoan == null) return null;
        taiKhoan.setRole(userRequest.getRole());
        if (userRequest.getTrangThaiHoatDong()==null) userRequest.setTrangThaiHoatDong(TrangThai.ACTIVE);
        taiKhoan.setTrangThaiHoatDong(userRequest.getTrangThaiHoatDong());
        return taiKhoan;
    }

    private User convertUserRequestToUser(UserRequest userRequest, User user, TaiKhoan taiKhoan){
        user.setHoTen(userRequest.getHoTen());
        user.setSdt(userRequest.getSdt());
        user.setCmnd(userRequest.getCmnd());
        user.setEmail(userRequest.getEmail());
        user.setDiaChi(userRequest.getDiaChi());
        user.setTaiKhoan(taiKhoan);
        return user;
    }

    private SignUpRequest convertUserRequestToSignUpRequest(UserRequest userRequest){
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setCmnd(userRequest.getCmnd());
        signUpRequest.setSdt(userRequest.getSdt());
        signUpRequest.setEmail(userRequest.getEmail());
        return signUpRequest;
    }

    public User getUserById(Long id){
        User user = userRepository.findUserById(id);
        if (user ==null) return null;
        return user;
    }

    @Override
    public List<User> getAll() {
        List<User> listUser = userRepository.findAll();
        if (listUser.size() == 0) return null;
        return listUser;
    }

    @Override
    public DataResponse updateUser(UserRequest userRequest, Long userId) {
        User user = userRepository.findUserById(userId);
        if (user == null) return new DataResponse("1","/");
        TaiKhoan taiKhoanNew = convertUserRequestToTaiKhoan(userRequest, userId);
        if (taiKhoanNew == null) return new DataResponse("2","/");
        User userNew = convertUserRequestToUser(userRequest, user, taiKhoanNew);
        int check = commonService.checkInForUpdateAccount(convertUserRequestToSignUpRequest(userRequest),taiKhoanNew);
        if (check != 5) return new DataResponse(String.valueOf(check+2),"/");
        taiKhoanRepository.save(taiKhoanNew);
        User userUpdate= userRepository.save(userNew);
        return new DataResponse("200",userUpdate);
    }
}
