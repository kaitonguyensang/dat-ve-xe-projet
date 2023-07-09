package com.example.datvexe.payloads.requests;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Setter
@Getter
public class TuyenXeRequestByAddressDate {
    private String benXeDen;
    private String benXeDi;
    private LocalDate date;
}
