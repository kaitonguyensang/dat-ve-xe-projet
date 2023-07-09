package com.example.datvexe.controllers;

import com.example.datvexe.handler.CustomException;
import com.example.datvexe.models.HangHoa;
import com.example.datvexe.models.VeXe;
import com.example.datvexe.payloads.requests.VeXeRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.services.VeXeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/vexe")
public class VeXeController {

    @Autowired
    VeXeService veXeService;

    @GetMapping("/user-id/{id}")
    public DataResponse getAllVexXeByUserId(@PathVariable("id") Long id){
        if(id == null) throw new CustomException("400","Khong ton tai user!!!!");
        List<VeXe> vexe = veXeService.getAllVeXeByUserId(id);
        if (vexe == null) throw new CustomException("404", "Khong ton tai user!!!");
        if (vexe.size() == 0) return new DataResponse("200", "Chua dat ve!!!");
        return new DataResponse("200",vexe);
    }

    @GetMapping("/tuyenxe-id/{id}")
    @PreAuthorize("hasAnyRole('NHAXE','ADMIN')")
    public DataResponse getAllVexXeByTuyenXeId(@PathVariable("id") String id){
        if(id == null) throw new CustomException("400","Missing field!!!!");
        Long tuyenXeId = Long.valueOf(id);
        List<VeXe> vexe = veXeService.getAllVeXeByTuyenXeId(tuyenXeId);
        if (vexe == null) throw new CustomException("404", "Khong ton tai tuyen xe!!!");
        if (vexe.size() == 0) return new DataResponse("200", "Chua dat ve!!!");
        return new DataResponse("200",vexe);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('USER','NHAXE')")
    public DataResponse addVeXe(@RequestBody VeXeRequest veXeRequest){
        if (veXeRequest == null) throw new CustomException("400","Missing field!!!");
        DataResponse dataResponse = veXeService.addVeXe(veXeRequest);
        if (dataResponse.getStatus()=="1") throw new CustomException("400", "Khong ton tai tuyen xe!!!");
        if (dataResponse.getStatus()=="2") throw new CustomException("400", "Khong ton tai user!!!");
        if (dataResponse.getStatus()=="3") throw new CustomException("400", "Ghe da duoc dat!!!");
        if (dataResponse.getStatus()=="4") throw new CustomException("400", "Dang ky khong thanh cong!!!");
        if (dataResponse.getStatus()=="5") return new DataResponse("200", dataResponse.getObject());
        return new DataResponse("200", dataResponse.getObject());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('NHAXE')")
    public DataResponse updateVeXe(@PathVariable("id") String id,@RequestBody VeXeRequest veXeRequest){
        if(veXeRequest == null || id==null)  throw  new CustomException("400","Missing field!!!");
        Long veXeId = Long.valueOf(id);
        VeXe veXe= veXeService.updateVeXe(veXeRequest,veXeId);
        if (veXe == null) throw new CustomException("404","Thay doi thong tin ve xe that bai!!!");
        return new DataResponse("200",veXe);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('USER','NHAXE')")
    public DataResponse deleteVeXe(@PathVariable("id") String id){
        if (id == null) throw new CustomException("400","Missing id!!!");
        Long veXeId = Long.valueOf(id);
        veXeId = veXeService.deleteVeXe(veXeId);
        if(veXeId == null) throw  new CustomException("404", "Khong tim thay ve xe");
        return new DataResponse("200", "Xoa thanh cong ve xe id: " + veXeId);
    }
}
