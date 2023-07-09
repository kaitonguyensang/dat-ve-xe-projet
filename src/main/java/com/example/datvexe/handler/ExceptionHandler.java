package com.example.datvexe.handler;

import com.example.datvexe.payloads.responses.DataResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public DataResponse handleRuntimeException(RuntimeException e) {
        e.printStackTrace();
        return new DataResponse("400", e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public DataResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        e.printStackTrace();
        return new DataResponse("400", e.getMessage());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.class)
    @ResponseBody
    public DataResponse handleCustomException(CustomException e) {
        e.printStackTrace();
        return new DataResponse(e.getStatus(), e.getMessage());
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public DataResponse handleCustomException(BadCredentialsException e) {
        e.printStackTrace();
        return new DataResponse("401", e.getMessage());
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(MessagingException.class)
    @ResponseBody
    public DataResponse handleMessagingException(MessagingException e) {
        e.printStackTrace();
        return new DataResponse("1011", e.getMessage());
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(UnsupportedEncodingException.class)
    @ResponseBody
    public DataResponse handleUnsupportedEncodingException(UnsupportedEncodingException e) {
        e.printStackTrace();
        return new DataResponse("1012", e.getMessage());
    }
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    @ResponseBody
    public DataResponse handleDefaultException(Exception e) {
        e.printStackTrace();
        return new DataResponse("500", e.getMessage());
    }

}
