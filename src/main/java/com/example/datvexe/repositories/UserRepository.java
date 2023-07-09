package com.example.datvexe.repositories;

import com.example.datvexe.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserById(Long id);
    User findUserByCmnd(String cmnd);
    User findUserBySdt(String sdt);
    User findUserByEmail(String email);

}
