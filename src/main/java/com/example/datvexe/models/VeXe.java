package com.example.datvexe.models;

import com.example.datvexe.common.HinhThucThanhToan;
import com.example.datvexe.common.TrangThai;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name ="vexe")
public class VeXe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "soghe")
    private int soGhe;

    @Column(name = "ngaydat")
    private LocalDate ngayDat;

    @Column(name = "ngaynhan")
    private LocalDate ngayNhan;

    @ManyToOne
    @JoinColumn(name = "tuyenxe_id",referencedColumnName = "id")
    @JsonManagedReference
    private TuyenXe tuyenXe;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    @JsonManagedReference
    private User user;

    @Column(name = "hinhthucthanhtoan")
    @Enumerated(EnumType.STRING)
    private HinhThucThanhToan hinhThucThanhToan;

    @Column(name = "xacthuc")
    @Enumerated(EnumType.STRING)
    private TrangThai trangThai;
}
