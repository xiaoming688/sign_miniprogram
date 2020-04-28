package com.sign.controller;

import com.sign.pojo.SignCreateDto;
import com.sign.pojo.SignDetailDto;
import com.sign.pojo.SignUserDto;
import com.sign.service.SignTeacherService;
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
 * @create 2020-04-27 15:27
 **/

@Api(tags = "签到共用相关", description = "")
@Slf4j
@RestController
@RequestMapping("/sign")
public class SignCommonController {

    @Autowired
    private SignUserService signUserService;

    @ApiOperation(value = "签到首页", notes = "")
    @RequestMapping(value = "/index", method = RequestMethod.POST)
    public MData index(@RequestBody @Validated SignUserDto signUserDto) {
        return signUserService.getIndex(signUserDto);
    }

    @ApiOperation(value = "新建签到", notes = "")
    @RequestMapping(value = "/createSign", method = RequestMethod.POST)
    public MData createSign(@RequestBody @Validated SignCreateDto signCreateDto) {
        return signUserService.createSign(signCreateDto);
    }

    @ApiOperation(value = "签到主页", notes = "")
    @RequestMapping(value = "/signDetail", method = RequestMethod.POST)
    public MData signDetail(@RequestBody @Validated SignDetailDto signDetailDto) {
        return signUserService.signDetail(signDetailDto);
    }

    @ApiOperation(value = "积分排行", notes = "")
    @RequestMapping(value = "/scoreRank", method = RequestMethod.POST)
    public MData scoreRank(@RequestBody @Validated SignDetailDto signDetailDto) {
        return signUserService.scoreRank(signDetailDto);
    }
}
