package com.example.datvexe.services.impl;

import com.example.datvexe.common.Provider;
import com.example.datvexe.common.TrangThai;
import com.example.datvexe.config.CustomTaiKhoanDetails;
import com.example.datvexe.models.TaiKhoan;
import com.example.datvexe.payloads.requests.TaiKhoanRequest;
import com.example.datvexe.repositories.TaiKhoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaiKhoanServiceImpl implements UserDetailsService {

    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    public void processOAuthPostLogin(String username) {
        TaiKhoan existTaiKhoan = taiKhoanRepository.findTaiKhoanByUsername(username);

        if (existTaiKhoan == null) {
            TaiKhoan newTaiKhoan = new TaiKhoan();
            newTaiKhoan.setUsername(username);
            newTaiKhoan.setProvider(Provider.GOOGLE);
            newTaiKhoan.setTrangThaiHoatDong(TrangThai.ACTIVE);

            taiKhoanRepository.save(newTaiKhoan);
        }

    }


    public TaiKhoan getTaiKhoanById(Long id){
        TaiKhoan taiKhoan = taiKhoanRepository.findTaiKhoanById(id);
        if (taiKhoan == null) return null;
        return taiKhoan;
    }

    public List<TaiKhoan> getAll(){
        List<TaiKhoan> listTaiKhoan = taiKhoanRepository.findAll();
        if (listTaiKhoan.size() == 0) return null;
        return listTaiKhoan;
    }

    public TaiKhoan updateTaiKhoan(TaiKhoanRequest taiKhoanRequest, Long id){
        TaiKhoan taiKhoanCheck = taiKhoanRepository.findTaiKhoanById(id);
        if(taiKhoanCheck == null) return null;
//        String passWordEncoded = commonService.changePasswordToPasswordEncode(taiKhoanRequest.getPassword());
//        String passWordEncoded = taiKhoanRequest.getPassword();
        taiKhoanCheck.setPassword(taiKhoanRequest.getPassword());
        taiKhoanCheck = taiKhoanRepository.save(taiKhoanCheck);
        return taiKhoanCheck;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TaiKhoan user = taiKhoanRepository.findTaiKhoanByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new CustomTaiKhoanDetails(user);
    }

    public UserDetails loadUserById(Long id){
        TaiKhoan user = taiKhoanRepository.findTaiKhoanById(id);
        if (user == null) {
            return null;
        }
        return new CustomTaiKhoanDetails(user);
    }
}
