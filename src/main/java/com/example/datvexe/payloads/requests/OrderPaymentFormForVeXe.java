package com.example.datvexe.payloads.requests;

import com.example.datvexe.common.OrderType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Data
public class OrderPaymentFormForVeXe {

    private OrderType orderType;

    private Long dongGia;

    private Long soLuong;

    private Long tongGiaTri;
}
