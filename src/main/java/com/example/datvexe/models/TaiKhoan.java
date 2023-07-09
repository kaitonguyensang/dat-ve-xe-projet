package com.example.datvexe.models;

import com.example.datvexe.common.Provider;
import com.example.datvexe.common.Role;
import com.example.datvexe.common.TrangThai;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "taikhoan")
public class TaiKhoan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    @JsonIgnore
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Provider provider;

    @Column(name = "trangthaihoatdong")
    @Enumerated(EnumType.STRING)
    private TrangThai trangThaiHoatDong;

    @JsonIgnore
    @Column(name="reset_pwd_code")
    private String resetPwdCode;

    @JsonIgnore
    @Column(name = "reset_pwd_time")
    private Date resetPwdTime;

    @JsonIgnore
    @Column(name = "attempt_forget_pwd")
    private Integer attemptCode;

    @OneToOne(mappedBy ="taiKhoan")
    @JsonBackReference
    private Admin admin;

    @OneToOne(mappedBy ="taiKhoan")
    @JsonBackReference
    private User user;

    @OneToOne(mappedBy ="taiKhoan")
    @JsonBackReference
    private NhaXe nhaXe;

    public TaiKhoan() {
    }

    public TaiKhoan(String username, String password, Role role, Provider provider, TrangThai trangThaiHoatDong) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.provider = provider;
        this.trangThaiHoatDong = trangThaiHoatDong;
    }
}
