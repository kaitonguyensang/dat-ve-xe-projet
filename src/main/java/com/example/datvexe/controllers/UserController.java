package com.example.datvexe.controllers;

import com.example.datvexe.handler.CustomException;
import com.example.datvexe.models.Admin;
import com.example.datvexe.models.NhaXe;
import com.example.datvexe.models.User;
import com.example.datvexe.payloads.requests.AdminRequest;
import com.example.datvexe.payloads.requests.UserRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.services.AdminService;
import com.example.datvexe.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public DataResponse getAll(){
        List<User> listUser = userService.getAll();
        if (listUser==null) throw new CustomException("404","Khong co Admin nao ca!!!");
        return new DataResponse("200",listUser);
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public DataResponse getById(@PathVariable("id") String id){
        if (id==null) throw new CustomException("400", "Missing field!!!");
        Long userId = Long.valueOf(id);
        User user = userService.getUserById(userId);
        if (user==null) throw new CustomException("404", "Khong ton tai admin nhu yeu cau!!!");
        return new DataResponse("200",user);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public DataResponse updateAdmin(@PathVariable("id") String id,@RequestBody UserRequest userRequest){
        if (userRequest == null || id == null) throw new CustomException("400", "Missing field!!!");
        Long userId = Long.valueOf(id);
        DataResponse dataResponse = userService.updateUser(userRequest,userId);
        //
        if (dataResponse.getStatus().equals("1")) throw new CustomException("404", "Khong ton tai nguoi dung!!!");
        if (dataResponse.getStatus().equals("2")) throw new CustomException("404", "Khong ton tai tai khoan!!!");
        if (dataResponse.getStatus().equals("3")) throw new CustomException("404", "So dien thoai da ton tai!!!");
        if (dataResponse.getStatus().equals("4")) throw new CustomException("404", "Ten nha xe da ton tai!!!");
        if (dataResponse.getStatus().equals("5")) throw new CustomException("404", "Email da ton tai!!!");
        if (dataResponse.getStatus().equals("6")) throw new CustomException("404", "CMND da ton tai!!!");
        return dataResponse;
    }


}
