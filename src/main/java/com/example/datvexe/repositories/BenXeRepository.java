package com.example.datvexe.repositories;

import com.example.datvexe.common.TrangThai;
import com.example.datvexe.models.BenXe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BenXeRepository extends JpaRepository<BenXe, Long> {
    List<BenXe> findAll();
    List<BenXe> findAllByTrangThai(TrangThai trangThai);
    BenXe findOneById(Long id);

    BenXe save(BenXe value);

    BenXe findBenXeByTenBenXeLike(String tenBenXe);
}
