package com.example.datvexe.controllers;

import com.example.datvexe.handler.CustomException;
import com.example.datvexe.payloads.requests.ThongKeNhaXeRequest;
import com.example.datvexe.payloads.responses.*;
import com.example.datvexe.payloads.requests.ThongKeAdminRequest;
import com.example.datvexe.services.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/thongke")
public class ThongKeController {

    @Autowired
    ThongKeService thongKeService;

    @GetMapping("/nguoidung/sao-trung-binh/{nhaxeid}")
    public DataResponse getSaoTrungBinh(@PathVariable("nhaxeid") String nhaxeid){
        if (nhaxeid == null) throw new CustomException("400","Missing field!!!");
        Long id =Long.valueOf(nhaxeid);
        float trungBinhSao = thongKeService.tinhTrungBinhSao(id);
        return new DataResponse("200",trungBinhSao);
    }

    @GetMapping("/nguoidung/sao-trung-binh/all")
    public DataResponse getSaoTrungBinhAll(){
        List<SaoTrungBinhAllResponse> saoTrungBinhAllResponses = thongKeService.getSaoTrungBinhAll();
        return new DataResponse("200",saoTrungBinhAllResponses);
    }

    @GetMapping("/nguoidung/so-sao/{nhaxeid}")
    public DataResponse getSoSao(@PathVariable("nhaxeid") String nhaxeid){
        if (nhaxeid == null) throw new CustomException("400","Missing field!!!");
        Long id =Long.valueOf(nhaxeid);
        ThongKeSaoResponse thongKeSao = thongKeService.thongKeSaoRequest(id);
        return new DataResponse("200",thongKeSao);
    }

    @PostMapping("/admin/so-don")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getThongKeAdmin(@RequestBody ThongKeAdminRequest thongKeAdminRequest){
        if (thongKeAdminRequest == null) throw new CustomException("400","Missing field!!!");
        List<ThongKeAdminUseResponse> thongKeAdminResponse = thongKeService.getThongKeAdminUse(thongKeAdminRequest);
        return new DataResponse("200",thongKeAdminResponse);
    }

    @PostMapping("/admin/doanh-thu")
    @PreAuthorize("hasRole('ADMIN')")
    public DataResponse getThongKeAdminDoanh(@RequestBody ThongKeAdminRequest thongKeAdminRequest){
        if (thongKeAdminRequest == null) throw new CustomException("400","Missing field!!!");
        List<ThongKeAdminDoanhThuResponse> thongKeAdminResponse = thongKeService.getThongKeAdminDoanhThu(thongKeAdminRequest);
        return new DataResponse("200",thongKeAdminResponse);
    }

    @PostMapping("/nhaxe/loai-xe")
    @PreAuthorize("hasAnyRole('ADMIN','NHAXE')")
    public DataResponse getThongKeNhaXeLoaiXe(@RequestBody ThongKeNhaXeRequest thongKeNhaXeRequest){
        if (thongKeNhaXeRequest == null) throw new CustomException("400","Missing field!!!");
        List<ThongKeNhaXeLoaiXeResponse> thongKeNhaXeLoaiXeResponses = thongKeService.getThongKeNhaXeLoaiXe(thongKeNhaXeRequest);
        return new DataResponse("200",thongKeNhaXeLoaiXeResponses);
    }

    @PostMapping("/nhaxe/tuyenxe")
    @PreAuthorize("hasAnyRole('ADMIN','NHAXE')")
    public DataResponse getThongKeNhaXeTuyenXe(@RequestBody ThongKeNhaXeRequest thongKeNhaXeRequest){
        if (thongKeNhaXeRequest == null) throw new CustomException("400","Missing field!!!");
        List<ThongKeNhaXeTuyenXeResponse> thongKeNhaXeTuyenXeResponses = thongKeService.getThongKeNhaXeTuyenXe(thongKeNhaXeRequest);
        return new DataResponse("200",thongKeNhaXeTuyenXeResponses);
    }

}
