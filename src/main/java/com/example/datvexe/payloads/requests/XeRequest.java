package com.example.datvexe.payloads.requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class XeRequest {
    private String bienSoXe;
    private String tenLoaiXe;
    private String tenNhaXe;
}
