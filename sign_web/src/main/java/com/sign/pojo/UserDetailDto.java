package com.sign.pojo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class UserDetailDto {

    @NotEmpty(message = "encryptedData不能为空")
    private String encryptedData;
    @NotEmpty(message = "iv不能为空")
    private String iv;

    private String appId;
    @NotEmpty(message = "openId不能为空")
    private String openId;

}
