package com.example.datvexe.services;

import com.example.datvexe.models.TuyenXe;
import com.example.datvexe.payloads.requests.TuyenXeRequest;
import com.example.datvexe.payloads.requests.TuyenXeRequestByAddress;
import com.example.datvexe.payloads.requests.TuyenXeRequestByAddressDate;
import com.example.datvexe.payloads.responses.TuyenXeChiTietResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface TuyenXeService {
    TuyenXe findOneById(Long id);
    List<TuyenXe> getAllBenXe();
    List<TuyenXe> getAllBenXeByAdmin();
    List<TuyenXe> getTuyenXeByBenXeDiBenXeDenNgayDi(TuyenXeRequest tuyenXeRequest);
    TuyenXe addNewTuyenXe(TuyenXeRequest tuyenXeRequest);
    List<TuyenXe> getTuyenXeByBenXeDiBenXeDenNgayDi(TuyenXeRequestByAddressDate tuyenXeRequest);
    List<TuyenXe> getTuyenXeByBenXeDiBenXeDen(TuyenXeRequestByAddress tuyenXeRequestByAddress);
    TuyenXe updateTuyenXe(TuyenXeRequest tuyenXeRequest, Long id);
    Long deleteTuyenXe(Long id);
}
