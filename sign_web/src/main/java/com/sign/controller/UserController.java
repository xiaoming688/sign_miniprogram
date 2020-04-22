package com.sign.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sign.model.User;
import com.sign.pojo.LoginParam;
import com.sign.pojo.UserDetailDto;
import com.sign.service.UserService;
import com.sign.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("/sign")
public class UserController {

    @Autowired
    private UserService userService;


    @RequestMapping(value = "/onLogin", method = RequestMethod.POST)
    public MData miniLogin(@RequestBody LoginParam loginParam) {
        MData result = new MData();
        String code = loginParam.getCode();

        if (StringUtils.isEmpty(code)) {
            log.error("code is {}", code);
            result.error("code is null");
            return result;
        }
        try {
            JSONObject userInfoObj = MpCompentent.getLoginInfo(Constants.APPID, Constants.APP_SECRET,
                    code, Constants.GRANT_TYPE);
            log.info("userInfoObj: " + userInfoObj);
            //该小程序openid，正常都会返回，不正常不会返回
            Object openid = userInfoObj.get("openid");
            //会话秘钥
            Object sessionKey = userInfoObj.get("session_key");
            //不正常直接返回，重新发起登录
            if (openid == null) {
                String errcode = userInfoObj.get("errcode").toString();
                String errmsg = userInfoObj.get("errmsg").toString();
                log.error("wx login:{} ", errcode + errmsg);
                result.error(errcode + errmsg);
                return result;
            }
            log.info("onLogin openId:" + openid + " sessionKey" + sessionKey);

            CacheUtil.cacheSessionMap.put(Constants.APPID + "_" + openid, sessionKey.toString());
            result.put("sessionKey", sessionKey);
            //用户持久化
            User user = userService.checkUserIsExist(String.valueOf(openid));
            result.put("isValid", true);
            result.put("openid", openid);
            result.put("uid", user.getId());
            result.put("nickName", user.getNickName());
            result.put("avatarUrl", user.getAvatarUrl());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.error("program error");
        }
        return result;
    }


    @RequestMapping(value = "/mini/userInfoDetail", method = RequestMethod.POST)
    public MData checkUserInfoDetail(@RequestBody UserDetailDto userDetailForm) {
        MData result = new MData();
        String appId = userDetailForm.getAppId();
        String openId = userDetailForm.getOpenId();
        try {
            String encryptedData = userDetailForm.getEncryptedData();
            String iv = userDetailForm.getIv();

            String sessionKey = CacheUtil.cacheSessionMap.get(appId + "_" + openId);
            log.info("sessionKey: " + sessionKey);
            //sessionKey不正确，前台重新发起login
            if (org.springframework.util.StringUtils.isEmpty(sessionKey)) {
                log.info("session_key is null");
                result.error("session_key is null");
                return result;
            }
            String decRes = AesCbcUtil.decrypt(encryptedData, sessionKey, iv, "UTF-8");
            if (!org.springframework.util.StringUtils.isEmpty(decRes)) {
                JSONObject desJson = JSON.parseObject(decRes);
                log.info("userInfoDetail desJson  : " + desJson.toJSONString());
                String nickname = String.valueOf(desJson.get("nickName"));
                String avatarUrl = String.valueOf(desJson.get("avatarUrl"));

                User user = userService.queryUserByOpenId(openId);
                if (user == null) {
                    user = new User();
                    user.setOpenId(openId);
                    user.setNickName(nickname);
                    user.setAvatarUrl(avatarUrl);
                    userService.addUser(user);
                } else if (user.getNickName() == null || user.getAvatarUrl() == null) {
                    user.setId(user.getId());
                    user.setNickName(nickname);
                    user.setAvatarUrl(avatarUrl);
                    userService.updateUser(user);
                }
                result.put("id", user.getId());
                result.put("faceImage", avatarUrl);
                result.put("nickName", nickname);
                result.put("openId", openId);
            } else {
                log.error("decrypt data failed");
                result.error("decrypt data failed");
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            result.error("exception with :" + ex.getMessage());
        } finally {
            CacheUtil.cacheSessionMap.remove(appId + "_" + openId);
        }
        return result;

    }
}
