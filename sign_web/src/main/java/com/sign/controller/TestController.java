package com.sign.controller;

import com.sign.model.User;
import com.sign.pojo.LoginDto;
import com.sign.service.UserService;
import com.sign.util.MData;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author MM
 * @description
 * @create 2020-04-22 15:11
 **/
@Api(tags = "ES 数据处理", description = "")
@Slf4j
@RestController
@RequestMapping("/sign")
public class TestController {

    @Autowired
    private UserService userService;

    @GetMapping("/getUser/{id}")
    public MData getUser(@PathVariable Integer id) {
        MData result = new MData();

        User user = userService.queryUserById(id);
        result.setData(user);
        log.info("getUser {}", user);
        return result;
    }

    @GetMapping("/getUserOpenId/{openId}")
    public MData getUserOpenId(@PathVariable String openId) {
        MData result = new MData();

        User user = userService.queryUserByOpenId(openId);
        result.setData(user);
        log.info("getUser {}", user);
        return result;
    }

}
