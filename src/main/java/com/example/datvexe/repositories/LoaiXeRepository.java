package com.example.datvexe.repositories;

import com.example.datvexe.models.LoaiXe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoaiXeRepository extends JpaRepository<LoaiXe, Long> {
    List<LoaiXe> findAll();

    LoaiXe findOneById(Long id);

    LoaiXe save(LoaiXe loaiXe);

    LoaiXe findLoaiXeByTenLoaiXe(String tenLoaiXe);
}
