package com.example.datvexe.repositories;
import com.example.datvexe.models.TaiKhoan;
import com.example.datvexe.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaiKhoanRepository extends JpaRepository<TaiKhoan, Long> {
    TaiKhoan findTaiKhoanByUser(User user);
    TaiKhoan findTaiKhoanByNhaXe_Email(String email);
    TaiKhoan findTaiKhoanById(Long id);
    List<TaiKhoan> findAll();
    TaiKhoan findTaiKhoanByUsername(String username);
    TaiKhoan save(TaiKhoan value);
    TaiKhoan findTaiKhoanByAdmin_Id(Long id);
    TaiKhoan findTaiKhoanByAdmin_Cmnd(String cmnd);
    TaiKhoan findTaiKhoanByAdmin_Email(String email);
    TaiKhoan findTaiKhoanByAdmin_Sdt(String sdt);
    TaiKhoan findTaiKhoanByUser_Cmnd(String cmnd);
    TaiKhoan findTaiKhoanByUser_Email(String email);
    TaiKhoan findTaiKhoanByUser_Sdt(String sdt);
    TaiKhoan findTaiKhoanByNhaXe_Sdt(String sdt);
    TaiKhoan findTaiKhoanByNhaXe_TenNhaXe(String tenNhaXe);

    TaiKhoan findTaiKhoanByNhaXe_Id(Long id);

    TaiKhoan findTaiKhoanByUser_Id(Long userId);
}
