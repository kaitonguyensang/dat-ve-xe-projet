package com.example.datvexe.payloads.requests;

import com.example.datvexe.common.Role;
import com.example.datvexe.common.TrangThai;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Getter
@Setter
public class TaiKhoanRequest {
    private String password;
}
