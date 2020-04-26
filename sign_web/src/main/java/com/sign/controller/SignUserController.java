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

/**
 * @author MM
 * @description
 * @create 2020-04-26 14:49
 **/
@Api(tags = "签到相关", description = "")
@Slf4j
@RestController
@RequestMapping("/sign")
public class SignUserController {

    @Autowired
    private SignUserService signUserService;

    @ApiOperation(value = "签到首页", notes = "")
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public MData index(@RequestBody @Validated SignUserDto signUserDto){
        return signUserService.getIndex(signUserDto);
    }

    @ApiOperation(value = "加入班级", notes = "")
    @RequestMapping(value = "/joinClass", method = RequestMethod.POST)
    public MData joinClass(@RequestBody @Validated JoinUserDto signUserDto){
        return signUserService.joinClass(signUserDto);
    }


    @ApiOperation(value = "新建签到", notes = "")
    @RequestMapping(value = "/createSign", method = RequestMethod.POST)
    public MData createSign(@RequestBody @Validated SignCreateDto signCreateDto){
        return signUserService.createSign(signCreateDto);
    }

    @ApiOperation(value = "签到详情", notes = "")
    @RequestMapping(value = "/signDetail", method = RequestMethod.POST)
    public MData signDetail(@RequestBody @Validated SignDetailDto signDetailDto){
        return signUserService.signDetail(signDetailDto);
    }

    @ApiOperation(value = "学生点击签到", notes = "")
    @RequestMapping(value = "/userSign", method = RequestMethod.POST)
    public MData userSign(@RequestBody @Validated UserSignDto userSignDto){
        return signUserService.userSign(userSignDto);
    }

}
