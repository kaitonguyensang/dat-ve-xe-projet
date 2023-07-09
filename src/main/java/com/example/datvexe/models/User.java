package com.example.datvexe.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "hoTen")
    private String hoTen;

    @Column(name ="cmnd")
    private String cmnd;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "email")
    private String email;

    @Column(name = "diachi")
    private String diaChi;

    @OneToOne
    @JoinColumn(name = "taikhoan_id",referencedColumnName = "id")
    @JsonManagedReference
    private TaiKhoan taiKhoan;

    @OneToMany(mappedBy ="user")
    @JsonBackReference
    private List<DanhGia> danhGia;

    @OneToMany(mappedBy ="user")
    @JsonIgnore
    private List<VeXe> veXe;

    @OneToMany(mappedBy ="user")
    @JsonIgnore
    private List<HangHoa> hangHoa;
}
