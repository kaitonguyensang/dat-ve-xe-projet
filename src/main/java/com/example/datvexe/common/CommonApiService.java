package com.example.datvexe.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommonApiService {
    @Autowired
    OTPService OTPService;

    @Autowired
    CommonAsyncService commonAsyncService;

    public String getOTPForgetPassword(){
        return OTPService.generate(6);
    }

    public String getOTPVerifyEmail(){
        return OTPService.generate(6);
    }

    public String getOTPValidateEmail(){
        return OTPService.generate(6);
    }

    public void sendEmail(String email, String msg, String subject, boolean html){
        commonAsyncService.sendEmail(email,msg,subject,html);
    }
}
