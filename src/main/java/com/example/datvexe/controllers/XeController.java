package com.example.datvexe.controllers;

import com.example.datvexe.handler.CustomException;
import com.example.datvexe.models.Xe;
import com.example.datvexe.payloads.requests.XeRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.services.XeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/xe")
public class XeController {

    @Autowired
    XeService xeService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public DataResponse getAll(){
        List<Xe> listXe = xeService.getAll();
        if (listXe.size()==0) throw  new CustomException("400", "Khong co xe nao!!!");
        return new DataResponse("200",listXe);
    }

    @GetMapping("/nhaxe/{nhaxe_id}")
    @PreAuthorize("hasAnyRole('ADMIN','NHAXE')")
    public DataResponse getAlByNhaXe(@PathVariable("nhaxe_id") String id){
        if (id == null) throw new CustomException("400","Missing field!!!");
        Long nhaXeId = Long.valueOf(id);
        List<Xe> listXe = xeService.getAllByNhaXeId(nhaXeId);
        if (listXe.size()==0) throw  new CustomException("400", "Khong co xe nao!!!");
        return new DataResponse("200",listXe);
    }

    @GetMapping("/{id}")
    public DataResponse getXeById(@PathVariable("id") String id){
        if (id == null) throw new CustomException("400","Missing field!!!");
        Long xeId = Long.valueOf(id);
        Xe xe = xeService.getById(xeId);
        if (xe == null) throw new CustomException("404","Khong tim thay xe!!!");
        return new DataResponse("200",xe);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN','NHAXE')")
    public DataResponse addXe(@RequestBody XeRequest xeRequest){
        if (xeRequest == null) throw new CustomException("400", "Missing field!!!");
        DataResponse dataResponse = xeService.addXe(xeRequest);
        if (dataResponse.getStatus() == "0") throw new CustomException("404", "Ten xe da ton tai!!!");
        if (dataResponse.getStatus() == "1") throw new CustomException("404", "Khong ton tai nha xe!!!");
        if (dataResponse.getStatus() == "2") throw new CustomException("404", "Khong ton tai loai xe!!!");
        return new DataResponse("200",dataResponse.getObject());
    }
}