package com.example.datvexe.models;
import com.example.datvexe.common.TrangThai;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name ="tuyenxe")
public class TuyenXe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "ngaydi")
    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate ngayDi;

    @Column(name = "giodi")
    private LocalTime gioDi;

    @Column(name = "thoigianhanhtrinh")
    private String thoiGianHanhTrinh;

    @Column(name = "giave")
    private int giaVe;

    @Column(name = "trangthai")
    @Enumerated(EnumType.STRING)
    private TrangThai trangThai;

    @ManyToOne
    @JoinColumn(name = "benxedi_id", referencedColumnName = "id")
    @JsonManagedReference
    private BenXe benXeDi;
    @ManyToOne
    @JoinColumn(name = "benxeden_id", referencedColumnName = "id")
    @JsonManagedReference
    private BenXe benXeDen;

    @OneToMany(mappedBy="tuyenXe")
    @JsonIgnore
    private List<VeXe> veXe;

    @OneToMany(mappedBy="tuyenXe")
    @JsonBackReference
    private List<HangHoa> hangHoa;

    @ManyToOne
    @JoinColumn(name = "xe_id", referencedColumnName = "id")
    @JsonManagedReference
    private Xe xe;

}
