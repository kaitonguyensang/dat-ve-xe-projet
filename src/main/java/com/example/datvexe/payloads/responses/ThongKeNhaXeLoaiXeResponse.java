package com.example.datvexe.payloads.responses;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ThongKeNhaXeLoaiXeResponse {
    private Long loaiXeId;
    private String tenLoaiXe;
    private float tyLe;
    private int tongDoanhThu;
}
