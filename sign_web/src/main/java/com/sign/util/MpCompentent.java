package com.sign.util;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.StringEntity;
import org.springframework.util.StringUtils;
import sun.misc.MessageUtils;

import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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

    public MpWxAccessToken getAccessToken(String appid, String secret) {
        //放缓存
        MpWxAccessToken accessToken = CacheUtil.accessTokenMap.get(appid);
        //判断access_token  是否存在
        if (accessToken == null) {
            log.info("get refresh");
            accessToken = new MpWxAccessToken(appid, secret,
                    getAccessTokenRequest(appid, secret), System.currentTimeMillis());
            CacheUtil.accessTokenMap.put(appid, accessToken);
        }
        //判断access_token 是否过期
        long nowTime = System.currentTimeMillis();
        if (nowTime - accessToken.getCreateTime() >= 7000 * 1000) {
            log.info("get refresh update");
            //过期重新获取
            accessToken = new MpWxAccessToken(appid, secret,
                    getAccessTokenRequest(appid, secret), System.currentTimeMillis());
            CacheUtil.accessTokenMap.put(appid, accessToken);
        }
        Date date = new Date(accessToken.getCreateTime());
        log.info("appid: " + appid + " accessTokenTime: " + DateUtil.formatDateTime(date) + " accessToken:" + accessToken.getAccessToken());
        return accessToken;
    }


    /**
     * 获取token
     *
     * @param appid
     * @param secret
     * @return
     */
    public static String getAccessTokenRequest(String appid, String secret) {
        Http http = new Http("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential");
        http.addParam("appid", appid);
        http.addParam("secret", secret);
        Http.HttpResult result = http.doGet();
        JSONObject json = result.toJsonObject();
        log.info("token :" + json);
        String accessToken = json.getString("access_token");
        if (StringUtils.isEmpty(accessToken)) {
            log.error("获取access_token 失败 appid :" + appid);
            throw new RuntimeException();
        }
        return accessToken;
    }

    public static MData sendMessageToOpenId(String accessToken, String openId) {
        MData result = new MData();
        try {
            String urlStr = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?access_token=" + accessToken;
            Http http = new Http(urlStr);

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("touser", openId);
            param.put("template_id", "pkKMKQzWL2w86yNN_JOSSRHCVr6T7rJn2FCebHB_SVU");

//            param.put("page", "pkKMKQzWL2w86yNN_JOSSRHCVr6T7rJn2FCebHB_SVU");
            //跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
            param.put("miniprogram_state", "trial");

            Map<String, Object> action_info = new HashMap();

            Map<String, Object> timeValue = new HashMap();
            timeValue.put("value", "2020-03-23 10:00~12:00");
            action_info.put("time1", timeValue);

            Map<String, Object> thing2Value = new HashMap();
            thing2Value.put("value", "英语口语课程");
            action_info.put("thing2", thing2Value);

            Map<String, Object> thing5Value = new HashMap();
            thing5Value.put("value", "刘老师");
            action_info.put("thing5", thing5Value);

            Map<String, Object> thing4Value = new HashMap();
            thing4Value.put("value", "英语口语速成班");
            action_info.put("thing4", thing4Value);

            param.put("data", action_info);

            JSONObject parseObject = JSONObject.parseObject(JSON.toJSONString(param));
            StringEntity stringEntity = new StringEntity(parseObject.toString(), Charset.forName("utf-8"));

            stringEntity.setContentEncoding("utf-8");
            stringEntity.setContentType("application/json");

            JSONObject sendResult = http.doPost(stringEntity).toJsonObject();
            String code = String.valueOf(sendResult.get("errcode"));
            log.info("sendResult: " + sendResult + " openId: " + openId);
            if (!code.equals("0")) {
                //找不到抛异常
                log.error("{}", sendResult);
            }
        } catch (Exception e) {
            log.error(e.getMessage() + ": send message to openId: " + openId);
        }
        return result;
    }
}
