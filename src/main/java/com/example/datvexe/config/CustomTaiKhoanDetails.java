package com.example.datvexe.config;

import com.example.datvexe.common.Provider;
import com.example.datvexe.common.Role;
import com.example.datvexe.common.TrangThai;
import com.example.datvexe.models.TaiKhoan;
import com.example.datvexe.models.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Data
public class CustomTaiKhoanDetails implements UserDetails {
    TaiKhoan user;

    public CustomTaiKhoanDetails (TaiKhoan user) {
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_"+user.getRole().toString()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
        public boolean isEnabled() {
            if (user.getTrangThaiHoatDong().equals(TrangThai.ACTIVE))
                return true;
            else return false;
    }
}

