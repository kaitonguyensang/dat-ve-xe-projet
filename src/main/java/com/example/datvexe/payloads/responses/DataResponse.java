package com.example.datvexe.payloads.responses;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ResponseBody;

@Getter
@Setter
@ResponseBody
@Slf4j
public class DataResponse {
    private String status;
    private String message;
    private Object object;

    public DataResponse(String status, String message) {
        this.status = status;
        this.message = message;
        this.object = null;
    }

    public DataResponse(String status, Object object) {
        this.status = status;
        this.object = object;
        this.message = null;
    }
}