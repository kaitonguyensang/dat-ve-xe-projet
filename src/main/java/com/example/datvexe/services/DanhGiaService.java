package com.example.datvexe.services;

import com.example.datvexe.models.DanhGia;
import com.example.datvexe.payloads.requests.DanhGiaRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DanhGiaService {
    DanhGia addDanhGia(DanhGiaRequest danhGiaRequest);
    DanhGia getDanhGiaById(Long id);
    List<DanhGia> getDanhGiaByNhaXeId(Long nhaXeId);
    Long deleteDanhGia(Long id);
}
