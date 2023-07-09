package com.example.datvexe.controllers;

import com.example.datvexe.handler.CustomException;
import com.example.datvexe.models.TuyenXe;
import com.example.datvexe.models.Xe;
import com.example.datvexe.payloads.requests.TuyenXeRequest;
import com.example.datvexe.payloads.requests.TuyenXeRequestByAddress;
import com.example.datvexe.payloads.requests.TuyenXeRequestByAddressDate;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.payloads.responses.TuyenXeChiTietResponse;
import com.example.datvexe.repositories.XeRepository;
import com.example.datvexe.services.TuyenXeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.xml.crypto.Data;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/tuyenxe")
public class TuyenXeController {
    @Autowired
    TuyenXeService tuyenXeService;

    @Autowired
    XeRepository xeRepository;

    @GetMapping("/{id}")
    public DataResponse addNewTuyenXe(@PathVariable("id") String id){
        if(id==null) throw new CustomException("400","Missing Field");
        Long tuyenXeId = Long.valueOf(id);
        TuyenXe tuyenXe = tuyenXeService.findOneById(tuyenXeId);
        if(tuyenXe==null) return new DataResponse("404","Khong tim duoc tuyen xe!!!");
        return new DataResponse("200", tuyenXe);
    }

    @GetMapping("/all")
    public DataResponse getAll(){
        List<TuyenXe> tuyenXeList = tuyenXeService.getAllBenXe();
        if(tuyenXeList.size()==0) throw new CustomException("404","Khong co tuyen xe nao!!!");
        return new DataResponse("200",tuyenXeList);
    }

    @GetMapping("/admin/all")
    public DataResponse getAllByAdmin(){
        List<TuyenXe> tuyenXeList = tuyenXeService.getAllBenXeByAdmin();
        if(tuyenXeList.size()==0) throw new CustomException("404","Khong co tuyen xe nao!!!");
        return new DataResponse("200",tuyenXeList);
    }

    @PostMapping("/find")
    public DataResponse getTuyenXeByBenXeDiBenXeDenNgayDi(@RequestBody TuyenXeRequest tuyenXeRequest){
        if(tuyenXeRequest == null)throw new CustomException("400","Missing request!!!");
        List<TuyenXe> tuyenXeList = tuyenXeService.getTuyenXeByBenXeDiBenXeDenNgayDi(tuyenXeRequest);
        if(tuyenXeList == null)throw new CustomException("404", "Khong tim thay tuyen xe");
        return new DataResponse("200",tuyenXeList);
    }

    @PostMapping("/find-by-address")
    public DataResponse findByAddress(@RequestBody TuyenXeRequestByAddress request){
        if (request == null) throw new CustomException("400","Missing request!!!");
        List<TuyenXe> tuyenXeList =tuyenXeService.getTuyenXeByBenXeDiBenXeDen(request);
        if (tuyenXeList.size() == 0) return new DataResponse("404","Khong co tuyen xe nao phu hop!!!");
        return new DataResponse("200",tuyenXeList);
    }

    @PostMapping("/find-by-address-date")
    public DataResponse findByAddressDate(@RequestBody TuyenXeRequestByAddressDate request){
        if (request == null) throw new CustomException("400","Missing request!!!");
        List<TuyenXe> tuyenXeList =tuyenXeService.getTuyenXeByBenXeDiBenXeDenNgayDi(request);
        if (tuyenXeList.size() == 0) return new DataResponse("404","Khong co tuyen xe nao phu hop!!!");
        return new DataResponse("200",tuyenXeList);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN','NHAXE')")
    public DataResponse addNewTuyenXe(@RequestBody TuyenXeRequest tuyenXeRequest){
        if(tuyenXeRequest == null)throw new CustomException("400", "Missing Field");
        Xe xe = xeRepository.findXeByBienSoXe(tuyenXeRequest.getBienSoXe());
        if (xe == null)throw new CustomException("404","Khong tim thay xe!!!");
        TuyenXe tuyenXe = tuyenXeService.addNewTuyenXe(tuyenXeRequest);
        if (tuyenXe==null)throw new CustomException("404","Khong tim thay ben xe!!!");
        return new DataResponse("200", tuyenXe);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','NHAXE')")
    public DataResponse updateTuyenXe(@PathVariable("id") String id, @RequestBody TuyenXeRequest tuyenXeRequest){
        if (id == null) throw new CustomException("400", "Missing field!!!");
        Long tuyenXeId = Long.valueOf(id);
        Xe xe = xeRepository.findXeByBienSoXe(tuyenXeRequest.getBienSoXe());
        if (xe == null)throw new CustomException("404","Khong tim thay xe!!!");
        TuyenXe tuyenXe = tuyenXeService.updateTuyenXe(tuyenXeRequest,tuyenXeId);
        if (tuyenXe == null)throw new CustomException("404", "Khong tim thay ben xe!!!");
        return new DataResponse("200", tuyenXe);
    }


    @DeleteMapping("/{id}")
//    @PreAuthorize("hasAnyRole('ADMIN','NHAXE')")
    public DataResponse deleteTuyenXe(@PathVariable("id") String id){
        if (id ==null) throw new CustomException("400", "Missing id!!!");
        Long tuyenXeId = Long.valueOf(id);
        tuyenXeId = tuyenXeService.deleteTuyenXe(tuyenXeId);
        if (tuyenXeId == null) throw new CustomException("404","Khong tim thay tuyen xe!!!");
        return new DataResponse("200", "Xoa thanh cong tuyen xe id: " + tuyenXeId);
    }
}
