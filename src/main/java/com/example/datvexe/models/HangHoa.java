package com.example.datvexe.models;
import com.example.datvexe.common.HinhThucThanhToan;
import com.example.datvexe.common.TrangThai;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "hanghoa")
public class HangHoa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cannang")
    private Long canNang;

    @Column(name = "gia")
    private int gia;

    @Column(name = "tennguoinhan")
    private String tenNguoNhan;

    @Column(name = "sdtnguoinhan")
    private String sdtNguoiNhan;

    @Column(name = "email")
    private String email;

    @Column(name = "ngaydat")
    private LocalDate ngayDat;

    @Column(name = "ma_thanh_toan")
    private String maThanhToan;

    @Column(name = "trangthai")
    @Enumerated(EnumType.STRING)
    private TrangThai trangThai;

    @Column(name = "hinhthucthanhtoan")
    @Enumerated(EnumType.STRING)
    private HinhThucThanhToan hinhThucThanhToan;

    @ManyToOne
    @JoinColumn(name = "tuyenxe_id",referencedColumnName = "id")
    @JsonIgnore
    private TuyenXe tuyenXe;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    @JsonIgnore
    private User user;
}
