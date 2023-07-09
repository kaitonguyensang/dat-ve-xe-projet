package com.example.datvexe.payloads.requests;

import com.example.datvexe.models.BenXe;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class TuyenXeRequestByAddress {
    private String benXeDen;
    private String benXeDi;
}
