package com.example.datvexe.repositories;

import com.example.datvexe.models.DanhGia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DanhGiaRepository extends JpaRepository<DanhGia, Long> {
    DanhGia findDanhGiaByUser_IdAndNhaXe_Id(Long userId, Long nhaXeId);
    DanhGia findDanhGiaById(Long id);
    List<DanhGia> findDanhGiasByNhaXe_Id(Long nhaXeId);

}
