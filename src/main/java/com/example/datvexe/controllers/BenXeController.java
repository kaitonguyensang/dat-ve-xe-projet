package com.example.datvexe.controllers;

import com.example.datvexe.handler.CustomException;
import com.example.datvexe.models.BenXe;
import com.example.datvexe.payloads.requests.BenXeRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.services.BenXeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.datvexe.constants.Constants.*;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://duyvotruong.github.io"})
@RequestMapping("/api/benxe")
public class BenXeController {

    @Autowired
    BenXeService benXeService;

    @GetMapping("/benxeid/{id}")
    public DataResponse getBenXeById(@PathVariable("id") String id) {
        if (id == null) throw new CustomException("400", MISS_FIELD);
        Long benXeId = Long.valueOf(id);
        BenXe benXe = benXeService.findBenXeById(benXeId);
        if (benXe == null) throw new CustomException("404", NO_ANY_BUS_STATION);
        return new DataResponse("200", benXe);
    }

    @GetMapping("/all-admin")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public DataResponse getAllForAdmin() {
        List<BenXe> benXeList = benXeService.findAllBenXeForAdmin();
        if (benXeList.size() == 0) throw new CustomException("404", NO_ANY_BUS_STATION);
        return new DataResponse("200", benXeList);
    }

    @GetMapping("/all-user")
    public DataResponse getAllForUser() {
        List<BenXe> benXeList = benXeService.findAllBenXeForUser();
        if (benXeList.size() == 0) throw new CustomException("404", NO_ANY_BUS_STATION);
        return new DataResponse("200", benXeList);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public DataResponse addNewBenXe(@RequestBody BenXeRequest benXeRequest) {
        if (benXeRequest == null) throw  new CustomException("400", MISS_FIELD);
        BenXe newBenXe = benXeService.addNewBenXe(benXeRequest);
        if (newBenXe==null) throw new CustomException("400", EXIST_BUS_NAME);
        return new DataResponse("200", newBenXe);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public DataResponse updateBenXe(@PathVariable("id") String id, @RequestBody BenXeRequest benXeRequest) {
        if (benXeRequest == null) throw  new CustomException("400", MISS_FIELD);
        Long benXeId = Long.valueOf(id);
        BenXe benXe = benXeService.updateBenXe(benXeRequest,benXeId);
        if (benXe == null) throw new CustomException("404", NO_ANY_BUS_STATION);
        return new DataResponse("200", benXe);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public DataResponse deleteBenXe(@PathVariable("id") String id) {
        Long benXeId = Long.valueOf(id);
        benXeId = benXeService.deleteBenXe(benXeId);
        if(benXeId == null) throw  new CustomException("404", NO_ANY_BUS_STATION);
        return new DataResponse("200", "Success full delete bus garage at id: " + benXeId);
    }
}
