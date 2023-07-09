package com.example.datvexe.services.impl;

import com.example.datvexe.common.TrangThai;
import com.example.datvexe.models.BenXe;
import com.example.datvexe.models.HangHoa;
import com.example.datvexe.payloads.requests.HangHoaRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.repositories.HangHoaRepository;
import com.example.datvexe.repositories.TuyenXeRepository;
import com.example.datvexe.repositories.UserRepository;
import com.example.datvexe.services.HangHoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HangHoaServiceImpl implements HangHoaService {

    @Autowired
    HangHoaRepository hangHoaRepository;

    @Autowired
    TuyenXeRepository tuyenXeRepository;

    @Autowired
    UserRepository userRepository;

    private DataResponse convertHangHoaRequestAddToHangHoa(HangHoaRequest hangHoaRequest){
        HangHoa hangHoa = new HangHoa();
        hangHoa.setNgayDat(hangHoaRequest.getNgayDat());
        hangHoa.setCanNang(hangHoaRequest.getCanNang());
        hangHoa.setTenNguoNhan(hangHoaRequest.getTenNguoiNhan());
        hangHoa.setSdtNguoiNhan(hangHoaRequest.getSdtNguoiNhan());
        hangHoa.setEmail(hangHoaRequest.getEmail());
        if (tuyenXeRepository.findOneById(hangHoaRequest.getTuyenXeId())==null) return new DataResponse("1","/");
        hangHoa.setTuyenXe(tuyenXeRepository.findOneById(hangHoaRequest.getTuyenXeId()));
        if (userRepository.findUserById(hangHoaRequest.getUserId())==null) return new DataResponse("2","/");
        hangHoa.setUser(userRepository.findUserById(hangHoaRequest.getUserId()));
        return new DataResponse("3", hangHoa);
    }

    @Override
    public DataResponse addHangHoaByUser(HangHoaRequest hangHoaForUserRequest){
        DataResponse dataResponse = convertHangHoaRequestAddToHangHoa(hangHoaForUserRequest);
        if (dataResponse.getStatus()!="3") return dataResponse;
        HangHoa hangHoa= (HangHoa) dataResponse.getObject();
        hangHoa.setTrangThai(TrangThai.INACTIVE);
        HangHoa hangHoaAdd = hangHoaRepository.save(hangHoa);
        return new DataResponse("200", hangHoaAdd);
    }


    ///
    private HangHoa convertHangHoaRequestUpdateToHangHoa(HangHoaRequest hangHoaRequest, HangHoa hangHoa){
        hangHoa.setCanNang(hangHoaRequest.getCanNang());
        hangHoa.setGia(hangHoaRequest.getGia());
        hangHoa.setTrangThai(hangHoaRequest.getTrangThai());
        return hangHoa;
    }

    @Override
    public HangHoa updateHangHoa(HangHoaRequest hangHoaRequest, Long hangHoaId){
        HangHoa hangHoaCheck = hangHoaRepository.findHangHoaById(hangHoaId);
        if (hangHoaCheck==null) return null;
        HangHoa hangHoaAdd = convertHangHoaRequestUpdateToHangHoa(hangHoaRequest, hangHoaCheck);
        HangHoa hangHoaNew = hangHoaRepository.save(hangHoaAdd);
        return hangHoaNew;
    }

    @Override
    public Long deleteHangHoa(Long hangHoaId) {
        HangHoa hangHoa = hangHoaRepository.findHangHoaById(hangHoaId);
        if (hangHoa == null) return null;
        hangHoaRepository.delete(hangHoa);
        return hangHoa.getId();
    }

    @Override
    public List<HangHoa> getHangHoaByUserId(Long userId) {
        List<HangHoa> hangHoaList = hangHoaRepository.findHangHoaByUser_Id(userId);
        return hangHoaList;
    }

    @Override
    public List<HangHoa> getHangHoaByTuyenXeId(Long tuyenXeId) {
        List<HangHoa> hangHoaList = hangHoaRepository.findHangHoaByTuyenXe_Id(tuyenXeId);
        return hangHoaList;
    }
}
