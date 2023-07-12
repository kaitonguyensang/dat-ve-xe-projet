package com.example.datvexe.controllers;

import com.example.datvexe.handler.CustomException;
import com.example.datvexe.models.NhaXe;
import com.example.datvexe.payloads.requests.NhaXeRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.payloads.responses.NhaXeResponse;
import com.example.datvexe.services.NhaXeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://duyvotruong.github.io"})
@RequestMapping("/api/nhaxe")
public class NhaXeController {

    @Autowired
    NhaXeService nhaXeService;

    @GetMapping("/user/all")
    public DataResponse getAllForUser(){
        List<NhaXeResponse> listNhaXe = nhaXeService.getAllForUser();
        if (listNhaXe.size() == 0) throw new CustomException("404","Khong co nha xe nao!!!");
        return new DataResponse("200",listNhaXe);
    }

    @GetMapping("/user/{id}")
    public DataResponse getByIdForUser(@PathVariable("id") String id){
        if (id==null) throw new CustomException("400", "Missing field!!!");
        Long nhaXeId = Long.valueOf(id);
        NhaXeResponse nhaXe = nhaXeService.getNhaXeByIdForUser(nhaXeId);
        if (nhaXe==null) throw new CustomException("404", "Khong ton tai nha xe nhu yeu cau!!!");
        return new DataResponse("200",nhaXe);
    }

    @GetMapping("/admin/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public DataResponse getAll(){
        List<NhaXe> listNhaXe = nhaXeService.getAll();
        if (listNhaXe.size() == 0) throw new CustomException("404","Khong co nha xe nao!!!");
        return new DataResponse("200",listNhaXe);
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','NHAXE')")
    public DataResponse getById(@PathVariable("id") String id){
        if (id==null) throw new CustomException("400", "Missing field!!!");
        Long nhaXeId = Long.valueOf(id);
        NhaXe nhaXe = nhaXeService.getNhaXeById(nhaXeId);
        if (nhaXe==null) throw new CustomException("404", "Khong ton tai nha xe nhu yeu cau!!!");
        return new DataResponse("200",nhaXe);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','NHAXE')")
    public DataResponse updateNhaXe(@PathVariable("id") String id,@RequestBody NhaXeRequest nhaXeRequest){
        if (nhaXeRequest == null || id == null) throw new CustomException("400", "Missing field!!!");
        Long nhaXeId = Long.valueOf(id);
        DataResponse dataResponse = nhaXeService.updateNhaXe(nhaXeRequest,nhaXeId);
        if (dataResponse.getStatus().equals("1")) throw new CustomException("404", "Khong ton tai nha xe!!!");
        if (dataResponse.getStatus().equals("2")) throw new CustomException("404", "Khong ton tai tai khoan!!!");
        if (dataResponse.getStatus().equals("3")) throw new CustomException("404", "So dien thoai da ton tai!!!");
        if (dataResponse.getStatus().equals("4")) throw new CustomException("404", "Ten nha xe da ton tai!!!");
        if (dataResponse.getStatus().equals("5")) throw new CustomException("404", "Email da ton tai!!!");
        if (dataResponse.getStatus().equals("6")) throw new CustomException("404", "CMND da ton tai!!!");
        return dataResponse;
    }
}
