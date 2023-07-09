package com.example.datvexe.services.impl;

import com.example.datvexe.common.TrangThai;
import com.example.datvexe.models.*;
import com.example.datvexe.payloads.requests.VeXeRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.repositories.TuyenXeRepository;
import com.example.datvexe.repositories.UserRepository;
import com.example.datvexe.repositories.VeXeRepository;
import com.example.datvexe.services.VeXeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VeXeServiceImpl implements VeXeService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    VeXeRepository veRepository;

    @Autowired
    TuyenXeRepository tuyenXeRepository;

    public VeXe convertVeXeRequestToVeXe(VeXeRequest veXeRequest, VeXe veXe, int soGhe) {
        veXe.setSoGhe(soGhe);
        veXe.setNgayDat(veXeRequest.getNgayDat());
        veXe.setNgayNhan(veXeRequest.getNgayNhan());
        veXe.setHinhThucThanhToan(veXeRequest.getHinhThucThanhToan());
        veXe.setTuyenXe(tuyenXeRepository.findOneById(veXeRequest.getTuyenXeId()));
        veXe.setUser(userRepository.findUserById(veXeRequest.getUserId()));
        veXe.setTrangThai(veXeRequest.getTrangThai());
        return veXe;
    }

    public List<VeXe> getAllVeXeByUserId(Long id){
        User user = userRepository.findUserById(id);
        if(user == null) return null;
        List<VeXe> veXeList = veRepository.getVeXeByUser(user);
        return veXeList;
    }

    private VeXe convertVeXeRequestUpdateToVeXe(VeXeRequest veXeRequest, VeXe veXe, int soGhe){
        veXe.setSoGhe(soGhe);
        veXe.setHinhThucThanhToan(veXeRequest.getHinhThucThanhToan());
        veXe.setTrangThai(veXeRequest.getTrangThai());
        veXe.setNgayNhan(veXeRequest.getNgayNhan());
        return veXe;
    }

    @Override
    public List<VeXe> getAllVeXeByTuyenXeId(Long tuyenXeId) {
        TuyenXe tuyenXe = tuyenXeRepository.findOneById(tuyenXeId);
        if(tuyenXe == null) return null;
        List<VeXe> veXeList = veRepository.findVeXeByTuyenXe(tuyenXe);
        if (veXeList == null) veXeList = new ArrayList<VeXe>();
        return veXeList;
    }


    @Override
    public DataResponse addVeXe(VeXeRequest veXeRequest) {
        TuyenXe tuyenXe = tuyenXeRepository.findOneById(veXeRequest.getTuyenXeId());
        if (tuyenXe == null) return new DataResponse("1","/");
        User user = userRepository.findUserById(veXeRequest.getUserId());
        if (user == null) return new DataResponse("2","/");
        List<VeXe> veXeNewList = new ArrayList<VeXe>();
        for(Integer soGhe : veXeRequest.getSoGhe()){
            VeXe veXeCheck = veRepository.findVeXeByTuyenXe_IdAndSoGhe(tuyenXe.getId(), soGhe);
            if (veXeCheck != null) return new DataResponse("3","/");
            veXeRequest.setTrangThai(TrangThai.INACTIVE);
            VeXe veXeNew = new VeXe();
            veXeNew = convertVeXeRequestToVeXe(veXeRequest,veXeNew,soGhe);
            if (veXeNew == null) return new DataResponse("4","/");
            veXeNewList.add(veRepository.save(veXeNew));
        }
        return new DataResponse("5", veXeNewList);
    }

    @Override
    public VeXe updateVeXe(VeXeRequest veXeRequest, Long veXeId) {
        VeXe veXeCheck = veRepository.findVeXeById(veXeId);
        if (veXeCheck==null) return null;
        Integer soGheCheck = 0;
        for (Integer soGhe : veXeRequest.getSoGhe())
            soGheCheck = soGhe;
        VeXe veXeAdd = convertVeXeRequestUpdateToVeXe(veXeRequest, veXeCheck,soGheCheck);
        VeXe veXeNew = veRepository.save(veXeAdd);
        return veXeNew;
    }

    @Override
    public Long deleteVeXe(Long veXeId) {
        VeXe veXeCheck = veRepository.findVeXeById(veXeId);
        if (veXeCheck == null) return null;
        veRepository.delete(veXeCheck);
        return veXeCheck.getId();
    }
}
