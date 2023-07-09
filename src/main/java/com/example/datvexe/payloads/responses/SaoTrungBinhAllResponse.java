package com.example.datvexe.payloads.responses;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class SaoTrungBinhAllResponse {
    private Long id;
    private Float saoTrungBinh;
}
