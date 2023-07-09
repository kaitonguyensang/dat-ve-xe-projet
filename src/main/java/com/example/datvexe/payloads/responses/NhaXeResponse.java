package com.example.datvexe.payloads.responses;

import com.example.datvexe.common.TrangThai;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class NhaXeResponse {
    private Long id;
    private String tenNhaXe;
    private String sdt;
    private String moTaNgan;
    private String diaChi;
    private Long taiKhoanId;
}
