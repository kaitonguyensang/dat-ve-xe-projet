package com.example.datvexe.models;

import com.example.datvexe.common.TrangThai;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "benxe")
public class BenXe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "tenbenxe")
    private String tenBenXe;

    @Column(name = "diachichitiet")
    private String diaChiChiTiet;

    @Column(name = "tinhthanh")
    private String tinhThanh;

    @Column(name = "trangthai")
    @Enumerated(EnumType.STRING)
    private TrangThai trangThai;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,mappedBy ="benXeDi")
    @JsonBackReference
    private List<TuyenXe> tuyenXeDi;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL,mappedBy ="benXeDen")
    @JsonBackReference
    private List<TuyenXe> tuyenXeDen;
}