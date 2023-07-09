package com.example.datvexe.services;

import com.example.datvexe.models.Xe;
import com.example.datvexe.payloads.requests.XeRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface XeService {
    List<Xe> getAll();

    List<Xe> getAllByNhaXeId(Long nhaXeId);

    Xe getById(Long id);

    DataResponse addXe(XeRequest value);
}
