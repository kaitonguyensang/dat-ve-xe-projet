package com.example.datvexe.services;

import com.example.datvexe.models.BenXe;
import com.example.datvexe.payloads.requests.BenXeRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BenXeService {

    List<BenXe> findAllBenXeForUser();

    List<BenXe> findAllBenXeForAdmin();

    BenXe findBenXeById(Long id);

    BenXe addNewBenXe(BenXeRequest benXeRequest);

    BenXe updateBenXe(BenXeRequest benXeRequest, Long id);

    Long deleteBenXe(Long id);
}
