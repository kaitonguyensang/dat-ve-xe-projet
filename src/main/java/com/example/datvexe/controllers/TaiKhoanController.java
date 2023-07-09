package com.example.datvexe.controllers;

import com.example.datvexe.handler.CustomException;
import com.example.datvexe.models.TaiKhoan;
import com.example.datvexe.payloads.requests.TaiKhoanRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.services.impl.TaiKhoanServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/taikhoan")
public class TaiKhoanController {

    @Autowired
    TaiKhoanServiceImpl taiKhoanService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/{id}")
    public DataResponse getTaiKhoanById(@PathVariable("id") String id) {
        if (id == null) throw new CustomException("404","Missing id!!");
        Long taiKhoanId = Long.valueOf(id);
        TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanById(taiKhoanId);
        if (taiKhoan == null) return new DataResponse("400","Khong co tai khoan!!!");
        return new DataResponse("200",taiKhoan);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public DataResponse getAllTaiKhoan(){
        List<TaiKhoan> listTaiKhoan = taiKhoanService.getAll();
        if (listTaiKhoan == null) return new DataResponse("404","Khong co tai khoan nao!!!");
        return new DataResponse("200",listTaiKhoan);
    }

    @PutMapping("{id}")
    public DataResponse updateTaiKhoan(@PathVariable("id") String id, @RequestBody TaiKhoanRequest taiKhoanRequest){
        if (taiKhoanRequest == null || id == null) throw new CustomException("400","Missing field!!!");
        Long taiKhoanId = Long.valueOf(id);
        String password = passwordEncoder.encode(taiKhoanRequest.getPassword());
        taiKhoanRequest.setPassword(password);
        TaiKhoan taiKhoanupdate =  taiKhoanService.updateTaiKhoan(taiKhoanRequest,taiKhoanId);
        if (taiKhoanupdate == null) throw new CustomException("404", "Tai khoan khong ton tai!!!");
        return new DataResponse("200",taiKhoanupdate);
    }
}
