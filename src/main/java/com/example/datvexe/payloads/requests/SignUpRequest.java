package com.example.datvexe.payloads.requests;


import com.example.datvexe.common.Role;
import com.example.datvexe.common.TrangThai;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;

@Data
@Getter
@Setter
public class SignUpRequest {

    @NotEmpty(message = "Email can not be null.")
    @ApiModelProperty(name = "idHash", required = true)
    private String idHash;

    @NotEmpty(message = "OPT can not be null.")
    @ApiModelProperty(name = "otp", required = true)
    private String otp;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String username;
    private String password;
    private String name;
    private String hoTen;
    private String tenNhaXe;
    private String sdt;
    private String cmnd;
    private String email;
    private String diaChi;
    private String moTaNgan;
    private TrangThai trangThaiHoatDong;
    private String picture;
}
