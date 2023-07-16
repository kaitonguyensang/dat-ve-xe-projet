package com.example.datvexe.controllers;

import com.example.datvexe.common.CommonApiService;
import com.example.datvexe.common.Provider;
import com.example.datvexe.common.Role;
import com.example.datvexe.common.TrangThai;
import com.example.datvexe.config.CustomTaiKhoanDetails;
import com.example.datvexe.config.JwtTokenProvider;
import com.example.datvexe.constants.Constants;
import com.example.datvexe.handler.CustomException;
import com.example.datvexe.models.TaiKhoan;
import com.example.datvexe.payloads.dto.ForgetPasswordDto;
import com.example.datvexe.payloads.requests.SignUpRequest;
import com.example.datvexe.payloads.responses.DataResponse;
import com.example.datvexe.payloads.responses.LoginResponse;
import com.example.datvexe.payloads.responses.VerifyEmailResponse;
import com.example.datvexe.repositories.TaiKhoanRepository;
import com.example.datvexe.services.SignUpService;
import com.example.datvexe.utils.AESUtils;
import com.example.datvexe.utils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.Objects;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://duyvotruong.github.io"})
@RequestMapping("/api/signup")
public class SignUpController {

    @Autowired
    CommonApiService commonApiService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    SignUpService signUpService;
    @Autowired
    private TaiKhoanRepository taiKhoanRepository;

    @PostMapping("/add")
    public DataResponse addTaiKhoan(@RequestBody SignUpRequest signUpRequest){
        int check = 0;
        TaiKhoan taiKhoan = new TaiKhoan();
        if (signUpRequest.getRole() == Role.USER){
            check = signUpService.addTaiKhoanUser(signUpRequest);
            if (check == 1) return new DataResponse("404","Username da ton tai!!");
            if (check == 2) return new DataResponse("404","CMND da ton tai!!");
            if (check == 3) return new DataResponse("404","So dien thoai da ton tai!!");
            if (check == 4) return new DataResponse("404","Email da ton tai!!");
            if (check == 6) return new DataResponse("404","Dang ky khong thanh cong!!!");
            taiKhoan = taiKhoanRepository.findTaiKhoanByUser_Email(signUpRequest.getEmail());
        } else if (signUpRequest.getRole()==Role.NHAXE){
            check = signUpService.addTaiKhoanNhaXe(signUpRequest);
            if (check == 1) return new DataResponse("404","Username da ton tai!!");
            if (check == 2) return new DataResponse("404","CMND da ton tai!!");
            if (check == 3) return new DataResponse("404","So dien thoai da ton tai!!");
            if (check == 4) return new DataResponse("404","Email da ton tai!!");
            if (check == 6) return new DataResponse("404","Ten nha xe da ton tai");
            taiKhoan = taiKhoanRepository.findTaiKhoanByNhaXe_Email(signUpRequest.getEmail());
        } else if (signUpRequest.getRole()==Role.ADMIN){
            check = signUpService.addTaiKhoanAdmin(signUpRequest);
            if (check == 1) return new DataResponse("404","Username da ton tai!!");
            if (check == 2) return new DataResponse("404","CMND da ton tai!!");
            if (check == 3) return new DataResponse("404","So dien thoai da ton tai!!");
            if (check == 4) return new DataResponse("404","Email da ton tai!!");
            if (check == 6) return new DataResponse("404","Dang ky khong thanh cong!!!");
            taiKhoan = taiKhoanRepository.findTaiKhoanByAdmin_Email(signUpRequest.getEmail());
        }
        if (check != 5) {
            return new DataResponse("404","Tao tai khoan khong thanh cong!!!");
        }
        String otp = commonApiService.getOTPVerifyEmail();
        taiKhoan.setAttemptVerifyEmail(0);
        taiKhoan.setVerifyEmailCode(otp);
        taiKhoan.setVerifyEmailTime(new Date());
        taiKhoanRepository.save(taiKhoan);

        commonApiService.sendEmail(signUpRequest.getEmail(), "This is OTP of account: " + taiKhoan.getUsername() + "\n" + "OTP: "+otp , "Reset password",false);

        String hash = AESUtils.encrypt (taiKhoan.getId()+";"+otp, true);


        return new DataResponse("200",hash);
    }

    @PostMapping("/verify-email")
    public DataResponse xacthucEmail(@RequestBody VerifyEmailResponse verifyEmailResponse) {
        if(verifyEmailResponse == null) throw new CustomException("404", "Missing field!!!");



        String[] hashGenerate = AESUtils.decrypt(verifyEmailResponse.getIdHash(),true).split(";",2);
        Long id = ConvertUtils.convertStringToLong(hashGenerate[0]);
        if(Objects.equals(id,0)){
            throw new CustomException("400", "Wrong hash");
        }

        TaiKhoan taiKhoan = taiKhoanRepository.findById(id).orElse(null);
        if (taiKhoan == null ) {
            throw new CustomException("404", "Account not found.");
        }

        if(taiKhoan.getAttemptVerifyEmail() >= Constants.MAX_ATTEMPT_VERIFY_EMAIL){
            taiKhoan.setResetPwdTime(null);
            taiKhoan.setResetPwdCode(null);
            taiKhoan.setAttemptCode(null);
            taiKhoan.setTrangThaiHoatDong(TrangThai.INACTIVE);
            taiKhoanRepository.save(taiKhoan);
            throw new CustomException("400", "Account locked");
        }

        if(!taiKhoan.getVerifyEmailCode().equals(verifyEmailResponse.getOtp()) ||
                (new Date().getTime() - taiKhoan.getVerifyEmailTime().getTime() >= Constants.MAX_TIME_FORGET_PWD)){
            //tang so lan
            taiKhoan.setAttemptVerifyEmail(taiKhoan.getAttemptVerifyEmail()+1);
            taiKhoanRepository.save(taiKhoan);

            throw new CustomException("400", "Code invalid");
        }

        taiKhoan.setResetPwdTime(null);
        taiKhoan.setResetPwdCode(null);
        taiKhoan.setAttemptCode(null);
        taiKhoan.setVerifyEmail(true);
        taiKhoanRepository.save(taiKhoan);

        TaiKhoan taiKhoan1 = taiKhoanRepository.findTaiKhoanById(id);


        // Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        verifyEmailResponse.getUsername(),
                        verifyEmailResponse.getPassword()
                )
        );

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Long idAcount = 0L;
        String email;
        TaiKhoan taiKhoanAcount = taiKhoanRepository.findTaiKhoanByUsername(verifyEmailResponse.getUsername());
        if (taiKhoanAcount.getRole() == Role.NHAXE) {
            id = taiKhoanAcount.getNhaXe().getId();
            email = taiKhoanAcount.getNhaXe().getEmail();
        }
        else  if (taiKhoanAcount.getRole() == Role.ADMIN) {
            id = taiKhoanAcount.getAdmin().getId();
            email = taiKhoanAcount.getAdmin().getEmail();
        } else {
            if (Provider.GOOGLE.equals(taiKhoanAcount.getProvider())) {
                return new DataResponse("400","Account details is invalid.");
            }
            id = taiKhoanAcount.getUser().getId();
            email = taiKhoanAcount.getUser().getEmail();
        }

        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomTaiKhoanDetails) authentication.getPrincipal());

        return new DataResponse("200", new LoginResponse(jwt,taiKhoan.getRole(), id, taiKhoan.getUsername(), email));
    }
}
