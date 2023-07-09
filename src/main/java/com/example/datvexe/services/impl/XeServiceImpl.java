package com.example.datvexe.services.impl;

import com.example.datvexe.models.LoaiXe;
import com.example.datvexe.models.NhaXe;
import com.example.datvexe.models.Xe;
import com.example.datvexe.payloads.requests.XeRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.repositories.LoaiXeRepository;
import com.example.datvexe.repositories.NhaXeRepository;
import com.example.datvexe.repositories.XeRepository;
import com.example.datvexe.services.XeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class XeServiceImpl implements XeService {
    @Autowired
    XeRepository xeRepository;

    @Autowired
    NhaXeRepository nhaXeRepository;

    @Autowired
    LoaiXeRepository loaiXeRepository;

    private DataResponse convertXeRequestToXe(XeRequest xeRequest, Xe xe){
        xe.setBienSoXe(xeRequest.getBienSoXe());
        if (nhaXeRepository.findNhaXeByTenNhaXe(xeRequest.getTenNhaXe())==null) return new DataResponse("1","/");
        xe.setNhaXe(nhaXeRepository.findNhaXeByTenNhaXe(xeRequest.getTenNhaXe()));
        if (loaiXeRepository.findLoaiXeByTenLoaiXe(xeRequest.getTenLoaiXe())==null) return new DataResponse("2","/");
        xe.setLoaiXe(loaiXeRepository.findLoaiXeByTenLoaiXe(xeRequest.getTenLoaiXe()));
        return new DataResponse("200",xe);
    }

    @Override
    public List<Xe> getAll() {
        List<Xe> listXe = xeRepository.findAll();
        if(listXe == null) return null;
        return listXe;
    }

    @Override
    public List<Xe> getAllByNhaXeId(Long nhaXeId) {
        List<Xe> listXe = xeRepository.findXeByNhaXe_Id(nhaXeId);
        if (listXe == null) return null;
        return listXe;
    }

    @Override
    public Xe getById(Long id) {
        Xe xe = xeRepository.findOneById(id);
        if(xe == null) return null;
        return xe;
    }

    @Override
    public DataResponse addXe(XeRequest xeRequest) {
        Xe xe = xeRepository.findXeByBienSoXe(xeRequest.getBienSoXe());
        if (xe != null) return new DataResponse("0","/");
        Xe xeNew = new Xe();
        DataResponse dataResponse = convertXeRequestToXe(xeRequest,xeNew);
        if (dataResponse.getStatus()=="1") return new DataResponse("1","/");
        if (dataResponse.getStatus()=="2") return new DataResponse("2","/");
        if (dataResponse.getStatus()=="200"){
            xeNew = xeRepository.save(xeNew);
        }
        return new DataResponse("200", xeNew);
    }


}
