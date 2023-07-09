package com.example.datvexe.repositories;

import com.example.datvexe.models.Xe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface XeRepository extends JpaRepository<Xe, Long> {
    Xe findOneById(Long id);
    List<Xe> findAll();
    Xe save(Xe value);

    List<Xe> findXeByNhaXe_Id(Long id);
    Xe findXeByBienSoXe(String bienSoXe);
}
