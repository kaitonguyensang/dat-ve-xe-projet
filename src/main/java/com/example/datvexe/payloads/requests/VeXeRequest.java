package com.example.datvexe.payloads.requests;

import com.example.datvexe.common.HinhThucThanhToan;
import com.example.datvexe.common.TrangThai;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Data
public class VeXeRequest {
    private List<Integer> soGhe;
    private LocalDate ngayDat;
    private LocalDate ngayNhan;
    private HinhThucThanhToan hinhThucThanhToan;
    private Long tuyenXeId;
    private Long userId;
    private TrangThai trangThai;
}
