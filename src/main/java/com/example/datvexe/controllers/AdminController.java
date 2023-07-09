package com.example.datvexe.controllers;

import com.example.datvexe.handler.CustomException;
import com.example.datvexe.models.Admin;
import com.example.datvexe.payloads.requests.AdminRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.datvexe.constants.Constants.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getAll(){
        List<Admin> listAdmin = adminService.getAll();
        if (listAdmin==null) throw new CustomException("404", NO_ANY_ACCOUNT);
        return new DataResponse("200",listAdmin);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public DataResponse getById(@PathVariable("id") String id){
        if (id==null) throw new CustomException("400", "Missing field!!!");
        Long adminId = Long.valueOf(id);
        Admin admin = adminService.getAdminById(adminId);
        if (admin==null) throw new CustomException("404", NO_ACCOUNT);
        return new DataResponse("200",admin);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public DataResponse updateAdmin(@PathVariable("id") String id,@RequestBody AdminRequest adminRequest){
        if (adminRequest == null || id == null) throw new CustomException("400", "Missing field!!!");
        Long adminId = Long.valueOf(id);
        DataResponse dataResponse = adminService.updateAdmin(adminRequest,adminId);
        if (dataResponse.getStatus().equals("1")) throw new CustomException("404", NO_ACCOUNT);
        if (dataResponse.getStatus().equals("2")) throw new CustomException("404", NO_ACCOUNT);
        if (dataResponse.getStatus().equals("3")) throw new CustomException("404", EXIST_PHONE_NUMBER);
        if (dataResponse.getStatus().equals("4")) throw new CustomException("404", EXIST_BUS_NAME);
        if (dataResponse.getStatus().equals("5")) throw new CustomException("404", EXIST_EMAIL);
        if (dataResponse.getStatus().equals("6")) throw new CustomException("404", EXIST_IDENTITY_CODE);
        return dataResponse;
    }


}
