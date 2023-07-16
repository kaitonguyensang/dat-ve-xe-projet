package com.example.datvexe.controllers;

import com.example.datvexe.common.*;
import com.example.datvexe.config.CustomTaiKhoanDetails;
import com.example.datvexe.constants.Constants;
import com.example.datvexe.handler.CustomException;
import com.example.datvexe.models.Admin;
import com.example.datvexe.models.NhaXe;
import com.example.datvexe.models.User;
import com.example.datvexe.payloads.dto.ApiMessageDto;
import com.example.datvexe.payloads.dto.ForgetPasswordDto;
import com.example.datvexe.payloads.requests.AccountGGUpdateRequest;
import com.example.datvexe.payloads.requests.ForgetPasswordForm;
import com.example.datvexe.payloads.requests.RequestForgetPasswordForm;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.repositories.AdminRepository;
import com.example.datvexe.repositories.NhaXeRepository;
import com.example.datvexe.repositories.UserRepository;
import com.example.datvexe.utils.AESUtils;
import com.example.datvexe.utils.ConvertUtils;
import com.example.datvexe.utils.GoogleUtils;
import com.example.datvexe.config.JwtTokenProvider;
import com.example.datvexe.models.TaiKhoan;
import com.example.datvexe.payloads.requests.LoginRequest;
import com.example.datvexe.payloads.responses.LoginResponse;
import com.example.datvexe.repositories.TaiKhoanRepository;
import org.apache.http.client.ClientProtocolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.Validation;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

import static com.example.datvexe.constants.Constants.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"http://localhost:3000", "https://duyvotruong.github.io"})
public class LoginController {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    CommonApiService commonApiService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    TaiKhoanRepository taiKhoanRepository;

    @Autowired
    NhaXeRepository nhaXeRepository;

    @Autowired
    AdminRepository adminRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private GoogleUtils googleUtils;

    @GetMapping("/login-google")
    public DataResponse loginGoogle(@RequestParam String accessToken) throws ClientProtocolException, IOException {
        GooglePojo googlePojo = googleUtils.getUserInfo(accessToken);
        CustomTaiKhoanDetails userDetail = googleUtils.buildUser(googlePojo);
        if (userDetail == null) {
            return new DataResponse("400", EXIST_EMAIL);
        }
        // Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDetail.getUsername(),
                        PASS_GOOGLE_ACCOUNT + googlePojo.getEmail()
                )
        );
        TaiKhoan taiKhoan = taiKhoanRepository.findTaiKhoanByUser_Email(googlePojo.getEmail());
        Long id = taiKhoan.getUser().getId();

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomTaiKhoanDetails) authentication.getPrincipal());

        LoginResponse loginResponse = new LoginResponse(jwt, Role.USER, id, taiKhoan.getUsername(), googlePojo.getEmail());

        if (taiKhoan.getUser().getCmnd() == null || taiKhoan.getUser().getSdt() == null || taiKhoan.getUser().getDiaChi() == null) {
            return new DataResponse("201", loginResponse);
        }

        return new DataResponse("200", loginResponse);
    }

    @PostMapping("/login-google/update")
    public DataResponse updateAccountLoginGG(@RequestBody AccountGGUpdateRequest accountGGUpdateRequest) {
        User user = userRepository.findUserById(accountGGUpdateRequest.getId());

        String cmnd = accountGGUpdateRequest.getCmnd();
        String sdt = accountGGUpdateRequest.getSdt();

        CustomException customExceptionCmnd = new CustomException("409", "Identity card has existed");
        CustomException customExceptionSdt = new CustomException("409", "Phone number has existed");

        //User
        User userold = userRepository.findUserByCmnd(cmnd);
        if(userold != null) throw customExceptionCmnd;
        userold = userRepository.findUserBySdt(sdt);
        if(userold != null) throw customExceptionSdt;

        //NhaXe
        NhaXe nhaXe = nhaXeRepository.findNhaXeBySdt(sdt);
        if(nhaXe != null) throw customExceptionSdt;

        //Admin
        Admin admin = adminRepository.findAdminByCmnd(cmnd);
        if(admin != null) throw customExceptionCmnd;
        admin = adminRepository.findAdminBySdt(sdt);
        if(admin != null) throw customExceptionSdt;

        user.setSdt(accountGGUpdateRequest.getSdt());
        user.setCmnd(accountGGUpdateRequest.getCmnd());
        user.setDiaChi(accountGGUpdateRequest.getDiaChi());


        userRepository.save(user);
        return new DataResponse("200", "Successfull! Add information for account.");
    }

    @PostMapping("/login")
//    @PreAuthorize("hasAnyRole('ADMIN')")
    public DataResponse authenticateUser(@RequestBody LoginRequest loginRequest) {

        // Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Long id = 0L;
        String email;
        TaiKhoan taiKhoan = taiKhoanRepository.findTaiKhoanByUsername(loginRequest.getUsername());
        if (taiKhoan.getRole() == Role.NHAXE) {
            id = taiKhoan.getNhaXe().getId();
            email = taiKhoan.getNhaXe().getEmail();
        }
        else  if (taiKhoan.getRole() == Role.ADMIN) {
            id = taiKhoan.getAdmin().getId();
            email = taiKhoan.getAdmin().getEmail();
        } else {
            if (Provider.GOOGLE.equals(taiKhoan.getProvider())) {
                return new DataResponse("400","Account details is invalid.");
            }
            id = taiKhoan.getUser().getId();
            email = taiKhoan.getUser().getEmail();
        }

        if (!taiKhoan.getVerifyEmail()) {
            return new DataResponse("201", new LoginResponse(null,taiKhoan.getRole(), id, taiKhoan.getUsername(), email));
        }

        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomTaiKhoanDetails) authentication.getPrincipal());

        if (taiKhoan.getRole() == Role.NHAXE) {
            NhaXe nhaXe = nhaXeRepository.findNhaXeById(taiKhoan.getNhaXe().getId());
            if (nhaXe.getNgayHetHan() == null) {
                nhaXe.setNgayHetHan(LocalDate.now().plusDays(-1));
                nhaXeRepository.save(nhaXe);
                return new DataResponse("204", new LoginResponse(jwt,taiKhoan.getRole(), id, taiKhoan.getUsername(), email));
            } else if (nhaXe.getNgayHetHan().isBefore(LocalDate.now())) {
                return new DataResponse("204", new LoginResponse(jwt,taiKhoan.getRole(), id, taiKhoan.getUsername(), email));
            }
        }

        return new DataResponse("200", new LoginResponse(jwt,taiKhoan.getRole(), id, taiKhoan.getUsername(), email));
    }

    @PostMapping("/request-forget-password")
    public ApiMessageDto<ForgetPasswordDto> requestForgetPassword(@RequestBody RequestForgetPasswordForm forgetForm) {
        ApiMessageDto<ForgetPasswordDto> apiMessageDto = new ApiMessageDto<>();
        TaiKhoan taiKhoan = taiKhoanRepository.findTaiKhoanByAdmin_Email(forgetForm.getEmail());
        if (taiKhoan == null) {
            taiKhoan = taiKhoanRepository.findTaiKhoanByNhaXe_Email(forgetForm.getEmail());
            if (taiKhoan == null) {
                taiKhoan = taiKhoanRepository.findTaiKhoanByUser_Email(forgetForm.getEmail());
                if (taiKhoan == null) {
                    throw new CustomException("404", "Account not found");
                }
                if (Provider.GOOGLE.equals(taiKhoan.getProvider())) {
                    throw new CustomException("400", "Can't request forget password for google account");
                }
            }
        }

        String email;
        if (Role.ADMIN.equals(taiKhoan.getRole())) {
            email = taiKhoan.getAdmin().getEmail();
        } else if (Role.NHAXE.equals(taiKhoan.getRole())) {
            email = taiKhoan.getNhaXe().getEmail();
        } else {
            email = taiKhoan.getUser().getEmail();
        }
        String otp = commonApiService.getOTPForgetPassword();
        taiKhoan.setAttemptCode(0);
        taiKhoan.setResetPwdCode(otp);
        taiKhoan.setResetPwdTime(new Date());
        taiKhoanRepository.save(taiKhoan);

        //send email
        commonApiService.sendEmail(email, "This is OTP of account: " + taiKhoan.getUsername() + "\n" + "OTP: "+otp , "Reset password",false);

        ForgetPasswordDto forgetPasswordDto = new ForgetPasswordDto();
        String hash = AESUtils.encrypt (taiKhoan.getId()+";"+otp, true);
        forgetPasswordDto.setIdHash(hash);
        apiMessageDto.setResult(true);
        apiMessageDto.setData(forgetPasswordDto);
        apiMessageDto.setMessage("Request forget password success, please check email.");
        return  apiMessageDto;
    }

    @PostMapping(value = "/forget-password", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiMessageDto<Long> forgetPassword(@Valid @RequestBody ForgetPasswordForm forgetForm, BindingResult bindingResult){
        ApiMessageDto<Long> apiMessageDto = new ApiMessageDto<>();

        String[] hash = AESUtils.decrypt(forgetForm.getIdHash(),true).split(";",2);
        Long id = ConvertUtils.convertStringToLong(hash[0]);
        if(Objects.equals(id,0L)){
            throw new CustomException("400", "Wrong hash");
        }

        TaiKhoan taiKhoan = taiKhoanRepository.findById(id).orElse(null);
        if (taiKhoan == null ) {
            throw new CustomException("404", "account not found.");
        }

        if(taiKhoan.getAttemptCode() >= Constants.MAX_ATTEMPT_FORGET_PWD){
            taiKhoan.setTrangThaiHoatDong(TrangThai.INACTIVE);
            taiKhoanRepository.save(taiKhoan);
            throw new CustomException("400", "Account locked");
        }

        if(!taiKhoan.getResetPwdCode().equals(forgetForm.getOtp()) ||
                (new Date().getTime() - taiKhoan.getResetPwdTime().getTime() >= Constants.MAX_TIME_FORGET_PWD)){
            //tang so lan
            taiKhoan.setAttemptCode(taiKhoan.getAttemptCode()+1);
            taiKhoanRepository.save(taiKhoan);

            throw new CustomException("400", "Code invalid");
        }

        taiKhoan.setResetPwdTime(null);
        taiKhoan.setResetPwdCode(null);
        taiKhoan.setAttemptCode(null);
        taiKhoan.setPassword(passwordEncoder.encode(forgetForm.getNewPassword()));
        taiKhoanRepository.save(taiKhoan);

        apiMessageDto.setResult(true);
        apiMessageDto.setMessage("Change password success.");
        return  apiMessageDto;
    }
}
