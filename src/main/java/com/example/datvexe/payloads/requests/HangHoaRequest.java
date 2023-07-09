package com.example.datvexe.payloads.requests;

import com.example.datvexe.common.TrangThai;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class HangHoaRequest {
    private Long canNang;
    private String tenNguoiNhan;
    private String sdtNguoiNhan;
    private int gia;
    private String email;
    private LocalDate ngayDat;
    private Long tuyenXeId;
    private Long userId;
    private TrangThai trangThai;
}
