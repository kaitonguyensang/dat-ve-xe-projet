package com.example.datvexe.services.impl;

import com.example.datvexe.common.TrangThai;
import com.example.datvexe.handler.CustomException;
import com.example.datvexe.models.Admin;
import com.example.datvexe.models.NhaXe;
import com.example.datvexe.models.TaiKhoan;
import com.example.datvexe.payloads.requests.NhaXeRequest;
import com.example.datvexe.payloads.requests.SignUpRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.payloads.responses.NhaXeResponse;
import com.example.datvexe.repositories.NhaXeRepository;
import com.example.datvexe.repositories.TaiKhoanRepository;
import com.example.datvexe.services.NhaXeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@Service
public class NhaXeServiceImpl implements NhaXeService {


    @Autowired
    NhaXeRepository nhaXeRepository;

    @Autowired
    CommonServiceImpl commonService;

    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    public TaiKhoan convertNhaXeRequestToTaiKhoan(NhaXeRequest nhaXeRequest, Long nhaXeId){
        TaiKhoan taiKhoan = taiKhoanRepository.findTaiKhoanByNhaXe_Id(nhaXeId);
        if (taiKhoan == null) return null;
        if (nhaXeRequest.getTrangThaiHoatDong()==null) nhaXeRequest.setTrangThaiHoatDong(TrangThai.ACTIVE);
        taiKhoan.setTrangThaiHoatDong(nhaXeRequest.getTrangThaiHoatDong());
        return taiKhoan;
    }

    public NhaXe convertNhaXeRequestToNhaXe(NhaXeRequest nhaXeRequest, NhaXe nhaXe, TaiKhoan taiKhoan){
        nhaXe.setTenNhaXe(nhaXeRequest.getTenNhaXe());
        nhaXe.setSdt(nhaXeRequest.getSdt());
        nhaXe.setEmail(nhaXeRequest.getEmail());
        nhaXe.setMoTaNgan(nhaXeRequest.getMoTaNgan());
        nhaXe.setDiaChi(nhaXeRequest.getDiaChi());
        nhaXe.setTaiKhoan(taiKhoan);
        nhaXe.setPicture(nhaXeRequest.getPicture());
        return nhaXe;
    }

    public SignUpRequest convertNhaXeRequestToSignUpRequest(NhaXeRequest nhaXeRequest){
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setSdt(nhaXeRequest.getSdt());
        signUpRequest.setMoTaNgan(nhaXeRequest.getMoTaNgan());
        signUpRequest.setDiaChi(nhaXeRequest.getDiaChi());
        signUpRequest.setEmail(nhaXeRequest.getEmail());
        signUpRequest.setTenNhaXe(nhaXeRequest.getTenNhaXe());
        signUpRequest.setTrangThaiHoatDong(nhaXeRequest.getTrangThaiHoatDong());
        signUpRequest.setRole(nhaXeRequest.getRole());
        return signUpRequest;
    }

    public NhaXeResponse convertNhaXeToNhaXeResponse(NhaXe nhaXe){
        NhaXeResponse nhaXeResponse = new NhaXeResponse();
        nhaXeResponse.setId(nhaXe.getId());
        nhaXeResponse.setSdt(nhaXe.getSdt());
        nhaXeResponse.setTenNhaXe(nhaXe.getTenNhaXe());
        nhaXeResponse.setMoTaNgan(nhaXe.getMoTaNgan());
        nhaXeResponse.setDiaChi(nhaXe.getDiaChi());
        nhaXeResponse.setTaiKhoanId(nhaXe.getTaiKhoan().getId());
        return nhaXeResponse;
    }

    @Override
    public List<NhaXe> getAll() {
        List<NhaXe> listNhaXe = nhaXeRepository.findAll();
        if (listNhaXe.size() == 0) return null;
        return listNhaXe;
    }

    @Override
    public List<NhaXeResponse> getAllForUser() {
        List<NhaXe> nhaXeList = nhaXeRepository.findNhaXesByTaiKhoanTrangThaiHoatDongLike(TrangThai.ACTIVE);
        nhaXeList.removeIf(nhaXe -> nhaXe.getNgayHetHan().isBefore(LocalDate.now()));
        List<NhaXeResponse> nhaXeResponseList = new ArrayList<NhaXeResponse>();
        for (NhaXe nhaXe : nhaXeList) {
            nhaXeResponseList.add(convertNhaXeToNhaXeResponse(nhaXe));
        }
        return nhaXeResponseList;
    }

    @Override
    public NhaXe getNhaXeById(Long id) {
        return nhaXeRepository.findNhaXeById(id);
    }

    @Override
    public NhaXeResponse getNhaXeByIdForUser(Long id) {
        NhaXe nhaXe = nhaXeRepository.findNhaXeByIdAndTaiKhoanTrangThaiHoatDongLike(id, TrangThai.ACTIVE);
        if (nhaXe.getNgayHetHan().isBefore(LocalDate.now())) {
            return null;
        }
        return convertNhaXeToNhaXeResponse(nhaXe);
    }

    @Override
    public DataResponse updateNhaXe(NhaXeRequest nhaXeRequest, Long nhaXeId) {
        NhaXe nhaXe = nhaXeRepository.findNhaXeById(nhaXeId);
        if (nhaXe == null) return new DataResponse("1","/");
        TaiKhoan taiKhoanNew = convertNhaXeRequestToTaiKhoan(nhaXeRequest, nhaXeId);
        if (taiKhoanNew == null) return new DataResponse("2","/");
        NhaXe nhaXeNew = convertNhaXeRequestToNhaXe(nhaXeRequest, nhaXe, taiKhoanNew);
        int check = commonService.checkInForUpdateAccount(convertNhaXeRequestToSignUpRequest(nhaXeRequest),taiKhoanNew);
        if (check != 5) return new DataResponse(String.valueOf(check+2),"/");
        taiKhoanRepository.save(taiKhoanNew);
        NhaXe nhaXeUpdate= nhaXeRepository.save(nhaXeNew);
        return new DataResponse("200",nhaXeUpdate);
    }
}
