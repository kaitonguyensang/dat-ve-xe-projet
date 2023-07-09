package com.example.datvexe.services.impl;

import com.example.datvexe.models.DanhGia;
import com.example.datvexe.payloads.requests.DanhGiaRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.repositories.DanhGiaRepository;
import com.example.datvexe.repositories.NhaXeRepository;
import com.example.datvexe.repositories.UserRepository;
import com.example.datvexe.services.DanhGiaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DanhGiaServiceImpl implements DanhGiaService {

    @Autowired
    DanhGiaRepository danhGiaRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    NhaXeRepository nhaXeRepository;

    public DanhGia convertDanhGiaRequestToDanhGia(DanhGiaRequest danhGiaRequest, DanhGia danhGia){
        danhGia.setSoSao(danhGiaRequest.getSoSao());
        danhGia.setNoiDung(danhGiaRequest.getNoiDung());
        danhGia.setGioDang(danhGiaRequest.getGioDang());
        danhGia.setNgayDang(danhGiaRequest.getNgayDang());
        danhGia.setUser(userRepository.findUserById(danhGiaRequest.getUserId()));
        danhGia.setNhaXe(nhaXeRepository.findNhaXeById(danhGiaRequest.getNhaXeId()));
        return danhGia;
    }

    public DanhGia addDanhGia(DanhGiaRequest danhGiaRequest){
        DanhGia danhGia =  danhGiaRepository.findDanhGiaByUser_IdAndNhaXe_Id(danhGiaRequest.getUserId(),danhGiaRequest.getNhaXeId());
        if (danhGia == null) {
            danhGia = new DanhGia();
        }
        DanhGia danhGiaNew = convertDanhGiaRequestToDanhGia(danhGiaRequest,danhGia);
        if (danhGiaNew == null) return null;
        danhGiaNew = danhGiaRepository.save(danhGiaNew);
        return danhGiaNew;
    }

    @Override
    public DanhGia getDanhGiaById(Long id) {
        DanhGia danhGia = danhGiaRepository.findDanhGiaById(id);
        if (danhGia == null) return null;
        return danhGia;
    }

    @Override
    public List<DanhGia> getDanhGiaByNhaXeId(Long nhaXeId) {
        List<DanhGia> danhGia = danhGiaRepository.findDanhGiasByNhaXe_Id(nhaXeId);
        if (danhGia.size()==0) return null;
        return danhGia;
    }

    @Override
    public Long deleteDanhGia(Long id) {
        DanhGia danhGia = danhGiaRepository.findDanhGiaById(id);
        if (danhGia == null) return null;
        danhGiaRepository.delete(danhGia);
        return danhGia.getId();
    }
}
