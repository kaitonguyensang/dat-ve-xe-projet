package com.example.datvexe.payloads.requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class AccountGGUpdateRequest {
    private Long id;
    private String cmnd;
    private String sdt;
    private String diaChi;
}
