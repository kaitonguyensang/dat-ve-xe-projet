package com.example.datvexe.services;

import com.example.datvexe.models.Admin;
import com.example.datvexe.payloads.requests.AdminRequest;
import com.example.datvexe.payloads.requests.SignUpRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AdminService {
    List<Admin> getAll();
    DataResponse updateAdmin(AdminRequest adminRequest, Long id);
    Admin getAdminById(Long id);
}
