package com.example.datvexe.config;

import com.example.datvexe.services.impl.TaiKhoanServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    TaiKhoanServiceImpl taiKhoanService;

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        // Get AuthenticationManager bean
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Password encoder, để Spring Security sử dụng mã hóa mật khẩu người dùng
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {
        auth.userDetailsService(taiKhoanService) // Cung cáp userservice cho spring security
                .passwordEncoder(passwordEncoder()); // cung cấp password encoder
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Khi người dùng đã login, với vai trò USER, Nhưng truy cập vào trang yêu cầu vai trò ADMIN, sẽ chuyển hướng tới trang /403
        http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
        http.csrf().disable()
                .cors() // Ngăn chặn request từ một domain khác
                .and()
                .authorizeRequests()
                .antMatchers("/api/login").permitAll()
                .antMatchers("/api/request-forget-password").permitAll()
                .antMatchers("/api/login-google").permitAll()
                .antMatchers("/api/forget-password").permitAll()
                .antMatchers("/api/tuyenxe/all").permitAll()
                .antMatchers("/api/signup/add").permitAll()
                .antMatchers("/api/tuyenxe/find-by-address-date").permitAll()
                .antMatchers("/api/tuyenxe/find-by-address").permitAll()
                .antMatchers("/api/benxe/benxeid/**").permitAll()
                .antMatchers("/api/benxe/all-user").permitAll()
                .antMatchers("/api/danhgia/nhaxe/**").permitAll()
                .antMatchers("/api/nhaxe/user/all").permitAll()
                .antMatchers("/api/nhaxe/user/**").permitAll()
                .antMatchers("/api/tuyenxe/update/**").permitAll()
                .antMatchers("/api/vexe/tuyenxe-id/**").permitAll()
                .antMatchers("/api/thongke/nguoidung/**").permitAll()// Cho phép tất cả mọi người truy cập vào địa chỉ này
                .anyRequest().authenticated(); // Tất cả các request khác đều cần phải xác thực mới được truy cập
        // Cấu hình cho Login Form.
        http.authorizeRequests().and().formLogin()//
                .loginProcessingUrl("/j_spring_security_login")//
                .loginPage("/login")//
                .defaultSuccessUrl("/user")//
                .failureUrl("/login?message=error")//
                .usernameParameter("username")//
                .passwordParameter("password")
                // Cấu hình cho Logout Page.
                .and().logout().logoutUrl("/j_spring_security_logout").logoutSuccessUrl("/login?message=logout");

        // Thêm một lớp Filter kiểm tra jwt
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
