package com.sign.pojo;

import lombok.Data;

/**
 * @author Canyon
 * @description
 * @date Created in 15:35 2018/1/5
 * @modified By
 */
@Data
public class LoginParam {
    private String code;

    private String encryptedData;

    private String iv;

    private String minType;

    private String appId;

    private String userToken;

}
