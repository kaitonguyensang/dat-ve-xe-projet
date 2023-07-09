package com.example.datvexe.payloads.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ThongKeNhaXeTuyenXeResponse {
    private String tinhThanhDi;
    private String tinhThanhDen;
    private float tyLe;
    private int tongDoanhThu;
}
