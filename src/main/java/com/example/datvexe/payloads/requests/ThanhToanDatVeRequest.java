package com.example.datvexe.payloads.requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Data
public class ThanhToanDatVeRequest {
    private List<Integer> soGheList;
    private Long tuyenXeId;
    private Integer tongSoTien;
}
