package com.example.datvexe.controllers;

import com.example.datvexe.common.CommonApiService;
import com.example.datvexe.common.GooglePojo;
import com.example.datvexe.common.Provider;
import com.example.datvexe.common.Role;
import com.example.datvexe.config.CustomTaiKhoanDetails;
import com.example.datvexe.constants.Constants;
import com.example.datvexe.handler.CustomException;
import com.example.datvexe.payloads.dto.ApiMessageDto;
import com.example.datvexe.payloads.dto.ForgetPasswordDto;
import com.example.datvexe.payloads.requests.ForgetPasswordForm;
import com.example.datvexe.payloads.requests.RequestForgetPasswordForm;
import com.example.datvexe.payloads.responses.DataResponse;
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
import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import static com.example.datvexe.constants.Constants.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class LoginController {

    private static final long serialVersionUID = 1L;

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
    UserRepository userRepository;

    @Autowired
    private GoogleUtils googleUtils;

    @GetMapping("/login-google")
    public DataResponse loginGoogle(HttpServletRequest request) throws ClientProtocolException, IOException {
        String code = request.getParameter("code");

        if (code == null || code.isEmpty()) {
            return new DataResponse("404", NOT_FOUND);
        }

        String accessToken = googleUtils.getToken(code);

        GooglePojo googlePojo = googleUtils.getUserInfo(accessToken);
        CustomTaiKhoanDetails userDetail = googleUtils.buildUser(googlePojo);
        if (userDetail == null) {
            return new DataResponse("400", EXIST_EMAIL);
        }
        TaiKhoan taiKhoan = taiKhoanRepository.findTaiKhoanByUser_Email(googlePojo.getEmail());
        Long id = userRepository.findUserByEmail(googlePojo.getEmail()).getId();
        LoginResponse loginResponse = new LoginResponse(null, Role.USER, id, taiKhoan.getUsername(), googlePojo.getEmail());
        if (!userDetail.isEnabled()) {
            return new DataResponse("201", loginResponse);
        }
        // Xác thực từ username và password.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userDetail.getUsername(),
                        userDetail.getPassword()
                )
        );

        // Nếu không xảy ra exception tức là thông tin hợp lệ
        // Set thông tin authentication vào Security Context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomTaiKhoanDetails) authentication.getPrincipal());
        loginResponse = new LoginResponse(jwt, Role.USER, id, taiKhoan.getUsername(), googlePojo.getEmail());
        return new DataResponse("200", loginResponse);
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

        // Trả về jwt cho người dùng.
        String jwt = tokenProvider.generateToken((CustomTaiKhoanDetails) authentication.getPrincipal());

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
        commonApiService.sendEmail(email,"OTP: "+otp, "Reset password",false);

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
        if(Objects.equals(id,0)){
            throw new CustomException("400", "Wrong hash");
        }

        TaiKhoan taiKhoan = taiKhoanRepository.findById(id).orElse(null);
        if (taiKhoan == null ) {
            throw new CustomException("404", "account not found.");
        }

        if(taiKhoan.getAttemptCode() >= Constants.MAX_ATTEMPT_FORGET_PWD){
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
