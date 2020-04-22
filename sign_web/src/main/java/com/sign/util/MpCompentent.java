package com.sign.util;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MpCompentent {
    public static JSONObject getLoginInfo(String appid, String secret, String jsCode, String grantType) {
        try {
            Http http = new Http("https://api.weixin.qq.com/sns/jscode2session");
            http.addParam("appid", appid);
            http.addParam("secret", secret);
            http.addParam("js_code", jsCode);
            http.addParam("grant_type", grantType);
            return http.doGet().toJsonObject();
        } catch (Exception e) {
            log.error("获取登录信息失败 :: " + e);
            throw new RuntimeException(e);
        }
    }
}
