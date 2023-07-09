package com.example.datvexe.payloads.requests;

import com.example.datvexe.common.TrangThai;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class BenXeRequest {
    private String tenBenXe;
    private String tinhThanh;
    private String diaChiChiTiet;
    private TrangThai trangThai;
}