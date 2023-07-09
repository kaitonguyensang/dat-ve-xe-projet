package com.example.datvexe.repositories;

import com.example.datvexe.common.TrangThai;
import com.example.datvexe.models.NhaXe;
import com.example.datvexe.models.TuyenXe;
import com.example.datvexe.models.User;
import com.example.datvexe.models.VeXe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VeXeRepository extends JpaRepository<VeXe, Long> {
    List<VeXe> getVeXeByUser(User user);

    VeXe findVeXeByTuyenXe_IdAndSoGhe(Long tuyenXeId, int soGhe);

    List<VeXe> findVeXeByTuyenXe(TuyenXe tuyenXe);
    VeXe findVeXeById(Long veXeId);

    List<VeXe> findVeXeByTrangThaiOrTrangThai(TrangThai trangThai1, TrangThai trangThai2);
}
