package com.sign.controller;

import com.sign.pojo.*;
import com.sign.service.SignUserService;
import com.sign.util.MData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author MM
 * @description
 * @create 2020-04-26 14:49
 **/
@Api(tags = "学生签到相关接口", description = "")
@Slf4j
@RestController
@RequestMapping("/sign/student")
public class SignStudentController {

    @Autowired
    private SignUserService signUserService;

    @ApiOperation(value = "加入班级", notes = "")
    @RequestMapping(value = "/joinClass", method = RequestMethod.POST)
    public MData joinClass(@RequestBody @Validated JoinUserDto signUserDto) {
        return signUserService.joinClass(signUserDto);
    }


    @ApiOperation(value = "学生点击签到", notes = "")
    @RequestMapping(value = "/userSign", method = RequestMethod.POST)
    public MData userSign(@RequestBody @Validated UserSignDto userSignDto) {
        return signUserService.userSign(userSignDto);
    }

    @ApiOperation(value = "加分记录", notes = "")
    @RequestMapping(value = "/scoreDetail", method = RequestMethod.POST)
    public MData scoreDetail(@RequestBody @Validated SignDetailDto signDetailDto) {
        Map<String, Object> data = signUserService.scoreDetail(signDetailDto);
        MData result = new MData();
        result.setData(data);
        return result;
    }

}
