package com.example.datvexe.repositories;

import com.example.datvexe.common.TrangThai;
import com.example.datvexe.models.NhaXe;
import com.example.datvexe.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.ListIterator;

@Repository
public interface NhaXeRepository extends JpaRepository<NhaXe, Long> {
    NhaXe findNhaXeByMaThanhToan(String maThanhToan);
    NhaXe findNhaXesByEmail(String email);
    NhaXe findNhaXeBySdt(String sdt);
    NhaXe findNhaXeByTenNhaXe(String tenNhaXe);
    NhaXe findNhaXeById(Long id);
    List<NhaXe> findNhaXesByTaiKhoanTrangThaiHoatDongLike(TrangThai trangThai);
    NhaXe findNhaXeByIdAndTaiKhoanTrangThaiHoatDongLike(Long id, TrangThai trangThai);
}
