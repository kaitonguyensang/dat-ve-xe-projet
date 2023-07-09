package com.example.datvexe.payloads.requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Data
public class ThongKeAdminRequest {
    private int month;
    private int year;
}
