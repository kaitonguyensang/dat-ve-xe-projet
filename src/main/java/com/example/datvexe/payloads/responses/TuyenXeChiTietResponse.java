package com.example.datvexe.payloads.responses;

import com.example.datvexe.common.TrangThai;
import com.example.datvexe.models.BenXe;
import com.example.datvexe.models.LoaiXe;
import com.example.datvexe.services.BenXeService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@ResponseBody
public class TuyenXeChiTietResponse {
    private Long id;//
    private String tenBenXeDi;
    private String tenBenXeDen;
    private String tenNhaXe;
    private String tenLoaiXe;
    private LocalDate ngayDi;//
    private LocalTime gioDi;//
    private String thoiGianHanhTrinh;//
    private Integer giaVe;//
    private TrangThai trangThai;//
    private int sucChua;
    private String sdt;
}
