package com.example.datvexe.services.impl;

import com.example.datvexe.models.LoaiXe;
import com.example.datvexe.payloads.requests.LoaiXeRequest;
import com.example.datvexe.repositories.LoaiXeRepository;
import com.example.datvexe.services.LoaiXeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoaiXeServiceImpl implements LoaiXeService {

    @Autowired
    LoaiXeRepository loaiXeRepository;

    public LoaiXe convertLoaiXeRequestToLoaiXe(LoaiXeRequest loaiXeRequest, LoaiXe loaiXe){
        loaiXe.setTenLoaiXe(loaiXeRequest.getTenLoaiXe());
        loaiXe.setSucChua(loaiXeRequest.getSucChua());
        return loaiXe;
    }

    public List<LoaiXe> getAllLoaiXe(){
      List<LoaiXe> listLoaiXe =   loaiXeRepository.findAll();
      if(listLoaiXe.size() == 0) return null;
      return listLoaiXe;
    }

    public LoaiXe getLoaiXeById(Long id) {
        LoaiXe loaiXe = loaiXeRepository.findOneById(id);
        if(loaiXe == null) return null;
        return loaiXe;
    }

    public LoaiXe addLoaiXe(LoaiXeRequest loaiXeRequest){
        LoaiXe loaiXe = loaiXeRepository.findLoaiXeByTenLoaiXe(loaiXeRequest.getTenLoaiXe());
        if(loaiXe != null) return null;
        LoaiXe loaiXeNew = new LoaiXe();
        loaiXeNew = convertLoaiXeRequestToLoaiXe(loaiXeRequest,loaiXeNew);
        return loaiXeRepository.save(loaiXeNew);
    }
}
