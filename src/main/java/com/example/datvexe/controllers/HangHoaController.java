package com.example.datvexe.controllers;

import com.example.datvexe.handler.CustomException;
import com.example.datvexe.models.HangHoa;
import com.example.datvexe.payloads.requests.HangHoaRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.services.HangHoaService;
import com.example.datvexe.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/hanghoa")
public class HangHoaController {

    @Autowired
    HangHoaService hangHoaService;

    @Autowired
    UserService userService;

    @GetMapping("/user/{user-id}")
    public DataResponse getHangHoaByUserId(@PathVariable("user-id") String userId){
        if (userId == null) throw new CustomException("400", "Missing field");
        Long id = Long.valueOf(userId);
        List<HangHoa> hangHoaList = hangHoaService.getHangHoaByUserId(id);
        if (hangHoaList == null) throw new CustomException("200", "Khong co hang hoa nao!!!!");
        return new DataResponse("200", hangHoaList);
    }

    @GetMapping("/tuyenxe/{tuyenxe-id}")
    @PreAuthorize("hasAnyRole('NHAXE','ADMIN')")
    public DataResponse getHangHoaByTuyenXeId(@PathVariable("tuyenxe-id") String tuyenXeId){
        if (tuyenXeId == null) throw new CustomException("400", "Missing field");
        Long id = Long.valueOf(tuyenXeId);
        List<HangHoa> hangHoaList = hangHoaService.getHangHoaByTuyenXeId(id);
        if (hangHoaList == null) throw new CustomException("200", "Khong co hang hoa nao!!!!");
        return new DataResponse("200", hangHoaList);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER','NHAXE')")
    public DataResponse addHangHoa(@RequestBody HangHoaRequest hangHoaRequest){
        if(hangHoaRequest == null)  throw  new CustomException("400","Missing field!!!");
        DataResponse dataResponse= hangHoaService.addHangHoaByUser(hangHoaRequest);
        if (dataResponse.getStatus() == "1") throw new CustomException("404","Tuyen xe khong ton tai!!!");
        if (dataResponse.getStatus() == "2") throw new CustomException("404","Nguoi dung khong ton tai!!!");
        return new DataResponse("200",dataResponse.getObject());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('NHAXE')")
    public DataResponse updateHangHoa(@PathVariable("id") String id, @RequestBody HangHoaRequest hangHoaRequest){
        if(hangHoaRequest == null || id==null)  throw  new CustomException("400","Missing field!!!");
        Long hangHoaId = Long.valueOf(id);
        HangHoa hangHoa= hangHoaService.updateHangHoa(hangHoaRequest,hangHoaId);
        if (hangHoa == null) throw new CustomException("404","Thay doi thong tin hang hoa that bai!!!");
        return new DataResponse("200",hangHoa);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('USER','NHAXE')")
    public DataResponse deleteHangHoa(@PathVariable("id") String id){
        if (id == null) throw new CustomException("400","Missing id!!!");
        Long hangHoaId = Long.valueOf(id);
        hangHoaId = hangHoaService.deleteHangHoa(hangHoaId);
        if(hangHoaId == null) throw  new CustomException("404", "Khong tim thay hang hoa");
        return new DataResponse("200", "Xoa thanh cong hang hoa id: " + hangHoaId);
    }
}
