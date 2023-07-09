package com.example.datvexe.services;

import com.example.datvexe.models.LoaiXe;
import com.example.datvexe.payloads.requests.LoaiXeRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface LoaiXeService {
    List<LoaiXe> getAllLoaiXe();

    LoaiXe getLoaiXeById(Long id);

    LoaiXe addLoaiXe(LoaiXeRequest loaiXeRequest);
}
