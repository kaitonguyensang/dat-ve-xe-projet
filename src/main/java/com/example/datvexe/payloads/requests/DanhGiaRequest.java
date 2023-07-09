package com.example.datvexe.payloads.requests;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Data
public class DanhGiaRequest {
    private int soSao;
    private String noiDung;
    private LocalTime gioDang;
    private LocalDate ngayDang;
    private Long userId;
    private Long nhaXeId;
}
