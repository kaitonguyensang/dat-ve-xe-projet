package com.example.datvexe.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "cmnd")
    private String cmnd;

    @Column(name = "sdt")
    private String sdt;

    @Column(name = "email")
    private String email;

    @OneToOne
    @JoinColumn(name = "taikhoan_id", referencedColumnName = "id")
    @JsonManagedReference
    private TaiKhoan taiKhoan;
}

