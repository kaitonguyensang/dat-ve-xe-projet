package com.example.datvexe.services.impl;

import com.example.datvexe.common.TrangThai;
import com.example.datvexe.models.BenXe;
import com.example.datvexe.models.TuyenXe;
import com.example.datvexe.models.Xe;
import com.example.datvexe.payloads.requests.TuyenXeRequest;
import com.example.datvexe.payloads.requests.TuyenXeRequestByAddress;
import com.example.datvexe.payloads.requests.TuyenXeRequestByAddressDate;
import com.example.datvexe.repositories.*;
import com.example.datvexe.services.TuyenXeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class TuyenXeServiceImpl implements TuyenXeService {
    @Autowired
    TuyenXeRepository tuyenXeRepository;

    @Autowired
    BenXeRepository benXeRepository;

    @Autowired
    XeRepository xeRepository;

    @Autowired
    HangHoaRepository hangHoaRepository;

    @Autowired
    VeXeRepository veXeRepository;

    public TuyenXe convertTuyenXeRequestToTuyenXe(TuyenXeRequest tuyenXeRequest, TuyenXe tuyenXe) {
        tuyenXe.setNgayDi(tuyenXeRequest.getNgayDi());
        tuyenXe.setGioDi(tuyenXeRequest.getGioDi());
        tuyenXe.setThoiGianHanhTrinh(tuyenXeRequest.getThoiGianHanhTrinh());
        tuyenXe.setGiaVe(tuyenXeRequest.getGiaVe());
        tuyenXe.setTrangThai(tuyenXeRequest.getTrangThai());
        BenXe benXeDi = benXeRepository.findBenXeByTenBenXeLike(tuyenXeRequest.getTenBenXeDi());
        if (benXeDi == null) return null;
        tuyenXe.setBenXeDi(benXeDi);
        BenXe benXeDen = benXeRepository.findBenXeByTenBenXeLike(tuyenXeRequest.getTenBenXeDen());
        if (benXeDen == null) return null;
        tuyenXe.setBenXeDen(benXeDen);
        Xe xe = xeRepository.findXeByBienSoXe(tuyenXeRequest.getBienSoXe());
        tuyenXe.setXe(xe);
        return tuyenXe;
    }

    public TuyenXe findOneById(Long id) {
        return tuyenXeRepository.findOneById(id);
    }

    public List<TuyenXe> getAllBenXe() {
        List<TuyenXe> tuyenXeList = tuyenXeRepository.findAll();
        tuyenXeList.removeIf(
                tuyenXe -> TrangThai.INACTIVE.equals(tuyenXe.getXe().getNhaXe().getTaiKhoan().getTrangThaiHoatDong()) && tuyenXe.getXe().getNhaXe()
                        .getNgayHetHan().isBefore(LocalDate.now()));
        return tuyenXeList;
    }

    public List<TuyenXe> getAllBenXeByAdmin() {
        return tuyenXeRepository.findAll();
    }

    public List<TuyenXe> getTuyenXeByBenXeDiBenXeDenNgayDi(TuyenXeRequest tuyenXeRequest) {
        BenXe benXeDi = benXeRepository.findBenXeByTenBenXeLike(tuyenXeRequest.getTenBenXeDi());
        if (benXeDi == null) return null;
        BenXe benXeDen = benXeRepository.findBenXeByTenBenXeLike(tuyenXeRequest.getTenBenXeDen());
        if (benXeDen == null) return null;
        List<TuyenXe> tuyenXeList = tuyenXeRepository.findTuyenXeByBenXeDiLikeAndBenXeDenLikeAndNgayDiLike(benXeDi, benXeDen, tuyenXeRequest.getNgayDi());
        tuyenXeList.removeIf(
                tuyenXe -> TrangThai.INACTIVE.equals(tuyenXe.getXe().getNhaXe().getTaiKhoan().getTrangThaiHoatDong()) && tuyenXe.getXe().getNhaXe()
                        .getNgayHetHan().isBefore(LocalDate.now()));
        return tuyenXeList;
    }

    public List<TuyenXe> getTuyenXeByBenXeDiBenXeDen(TuyenXeRequestByAddress tuyenXeRequest) {
        List<TuyenXe> tuyenXeList =  tuyenXeRepository.findTuyenXeByBenXeDi_TinhThanhContainsAndBenXeDen_TinhThanhContains(tuyenXeRequest.getBenXeDi(),
                tuyenXeRequest.getBenXeDen());
        tuyenXeList.removeIf(
                tuyenXe -> TrangThai.INACTIVE.equals(tuyenXe.getXe().getNhaXe().getTaiKhoan().getTrangThaiHoatDong()) && tuyenXe.getXe().getNhaXe()
                        .getNgayHetHan().isBefore(LocalDate.now()));
        return tuyenXeList;
    }

    @Override
    public TuyenXe updateTuyenXe(TuyenXeRequest tuyenXeRequest, Long id) {
        TuyenXe tuyenXe = tuyenXeRepository.findOneById(id);
        TuyenXe tuyenXeNew = convertTuyenXeRequestToTuyenXe(tuyenXeRequest, tuyenXe);
        if (tuyenXeNew == null) return null;
        return tuyenXeRepository.save(tuyenXeNew);
    }

    @Override
    @Transactional
    public Long deleteTuyenXe(Long id) {
        TuyenXe tuyenXeDelete = tuyenXeRepository.findOneById(id);
        //        if (tuyenXeDelete == null) return null;
        //        veXeRepository.deleteAllByTuyenXe(tuyenXeDelete);
        //        hangHoaRepository.deleteAllByTuyenXe(tuyenXeDelete);
        ////        tuyenXeRepository.delete(tuyenXeDelete);
        //        tuyenXeRepository.deleteTuyenXeById(id);
        ////        return tuyenXeDelete.getId();
        tuyenXeRepository.delete(tuyenXeDelete);
        return id;
    }

    public TuyenXe addNewTuyenXe(TuyenXeRequest tuyenXeRequest) {
        TuyenXe tuyenXe = new TuyenXe();
        tuyenXe = convertTuyenXeRequestToTuyenXe(tuyenXeRequest, tuyenXe);
        if (tuyenXe == null) return null;
        return tuyenXeRepository.save(tuyenXe);
    }

    @Override
    public List<TuyenXe> getTuyenXeByBenXeDiBenXeDenNgayDi(TuyenXeRequestByAddressDate tuyenXeRequest) {
        List<TuyenXe> tuyenXeList = tuyenXeRepository.findTuyenXeByBenXeDi_TinhThanhContainsAndBenXeDen_TinhThanhContainsAndAndNgayDiLike(tuyenXeRequest.getBenXeDi(),
                tuyenXeRequest.getBenXeDen(), tuyenXeRequest.getDate());
        tuyenXeList.removeIf(
                tuyenXe -> TrangThai.INACTIVE.equals(tuyenXe.getXe().getNhaXe().getTaiKhoan().getTrangThaiHoatDong()) && tuyenXe.getXe().getNhaXe()
                        .getNgayHetHan().isBefore(LocalDate.now()));
        return tuyenXeList;
    }

}
