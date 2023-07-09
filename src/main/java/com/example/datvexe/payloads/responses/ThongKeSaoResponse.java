package com.example.datvexe.payloads.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ThongKeSaoResponse {
    private int sao1;
    private int sao2;
    private int sao3;
    private int sao4;
    private int sao5;
    private int soNguoiDanhGia;
}
