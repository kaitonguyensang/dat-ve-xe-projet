package com.example.datvexe.payloads.requests;

import com.example.datvexe.common.Role;
import com.example.datvexe.common.TrangThai;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@Data
public class NhaXeRequest {
    private String tenNhaXe;
    private String sdt;
    private String moTaNgan;
    private String diaChi;
    private String email;
    @Enumerated(EnumType.STRING)
    private TrangThai trangThaiHoatDong;
    @Enumerated(EnumType.STRING)
    private Role role;
}
