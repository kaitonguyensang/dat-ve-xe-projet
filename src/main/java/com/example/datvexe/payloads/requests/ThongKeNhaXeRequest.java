package com.example.datvexe.payloads.requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class ThongKeNhaXeRequest {
    private int month;
    private int year;
    private Long nhaXeId;
}
