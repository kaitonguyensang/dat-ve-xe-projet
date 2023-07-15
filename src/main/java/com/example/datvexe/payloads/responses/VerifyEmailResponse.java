package com.example.datvexe.payloads.responses;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;

@Data
@ApiModel
public class VerifyEmailResponse {
    @NotEmpty(message = "OTP can not be null.")
    @ApiModelProperty(name = "otp", required = true)
    private String otp;

    @NotEmpty(message = "Email can not be null.")
    @ApiModelProperty(name = "idHash", required = true)
    private String idHash;
}
