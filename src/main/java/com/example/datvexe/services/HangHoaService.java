package com.example.datvexe.services;

import com.example.datvexe.models.HangHoa;
import com.example.datvexe.payloads.requests.HangHoaRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface HangHoaService {
    DataResponse addHangHoaByUser(HangHoaRequest hangHoaForUserRequest);

    HangHoa updateHangHoa(HangHoaRequest hangHoaForUserRequest, Long hangHoaId);
    Long deleteHangHoa(Long hangHoaId);
    List<HangHoa> getHangHoaByUserId(Long userId);
    List<HangHoa> getHangHoaByTuyenXeId(Long nhaXeId);
}
