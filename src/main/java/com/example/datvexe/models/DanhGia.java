package com.example.datvexe.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "danhgia")
public class DanhGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "sosao")
    private int soSao;

    @Column(name = "noidung")
    private String noiDung;

    @Column(name = "giodang")
    private LocalTime gioDang;

    @Column(name = "ngaydang")
    private LocalDate ngayDang;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "nhaxe_id", referencedColumnName = "id")
    @JsonManagedReference
    private NhaXe nhaXe;

}