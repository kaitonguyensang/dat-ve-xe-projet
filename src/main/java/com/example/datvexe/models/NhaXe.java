package com.example.datvexe.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "nhaxe")
public class NhaXe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tennhaxe")
    private String tenNhaXe;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "motangan")
    private String moTaNgan;

    @Column(name = "email")
    private String email;

    @Column(name = "diachi")
    private String diaChi;

    @Column(name = "ma_thanh_toan")
    private String maThanhToan;

    @Column(name = "ngayhethan")
    private LocalDate ngayHetHan;

    @Column(name = "picture")
    private String picture;

    @OneToOne(mappedBy ="nhaXe")
    @JsonManagedReference
    private TaiKhoan taiKhoan;

    @OneToMany(mappedBy ="nhaXe")
    @JsonIgnore
    private List<Xe> xe;

    @OneToMany(mappedBy ="nhaXe")
    @JsonIgnore
    private List<DanhGia> danhGia;
}
