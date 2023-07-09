package com.example.datvexe.payloads.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ThongKeAdminDoanhThuResponse {
    private Long nhaXeId;
    private String tenNhaXe;
    private float tyLeDoanhThu;
    private int doanhThu;
}
