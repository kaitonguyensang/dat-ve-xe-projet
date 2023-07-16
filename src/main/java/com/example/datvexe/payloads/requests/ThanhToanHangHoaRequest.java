package com.example.datvexe.payloads.requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ThanhToanHangHoaRequest {
    private Long tuyenXeId;
    private Integer tongSoTien;
    private Long userId;
}
