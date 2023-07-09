package com.example.datvexe.services;

import com.example.datvexe.payloads.requests.ThongKeNhaXeRequest;
import com.example.datvexe.payloads.responses.*;
import com.example.datvexe.payloads.requests.ThongKeAdminRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public interface ThongKeService {
    float tinhTrungBinhSao(Long nhaXeId);

    ThongKeSaoResponse thongKeSaoRequest(Long nhaXeId);

    List<SaoTrungBinhAllResponse> getSaoTrungBinhAll();

    List<ThongKeAdminUseResponse> getThongKeAdminUse(ThongKeAdminRequest request);

    List<ThongKeAdminDoanhThuResponse> getThongKeAdminDoanhThu(ThongKeAdminRequest request);

    List<ThongKeNhaXeLoaiXeResponse> getThongKeNhaXeLoaiXe(ThongKeNhaXeRequest request);

    List<ThongKeNhaXeTuyenXeResponse> getThongKeNhaXeTuyenXe(ThongKeNhaXeRequest request);
}
