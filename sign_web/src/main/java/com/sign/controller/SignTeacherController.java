package com.sign.controller;

import com.sign.pojo.AddSignScoreDto;
import com.sign.pojo.SignClassUserDto;
import com.sign.pojo.SignDetailDto;
import com.sign.pojo.SignTaskDto;
import com.sign.service.SignTeacherService;
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
import springfox.documentation.annotations.ApiIgnore;

/**
 * @author MM
 * @description
 * @create 2020-04-27 15:20
 **/
@Api(tags = "老师签到相关接口", description = "")
@Slf4j
@RestController
@RequestMapping("/sign/teacher")
public class SignTeacherController {

    @Autowired
    private SignTeacherService signTeacherService;

    @ApiOperation(value = "布置作业--发起新签到", notes = "")
    @RequestMapping(value = "/createSignTask", method = RequestMethod.POST)
    public MData createSignTask(@RequestBody @Validated SignTaskDto signTaskDto){
        return signTeacherService.createSignTask(signTaskDto);
    }

    @ApiOperation(value = "布置作业--添加积分", notes = "")
    @RequestMapping(value = "/addSingScore", method = RequestMethod.POST)
    public MData addSingScore(@RequestBody @Validated AddSignScoreDto addSignScoreDto){
        return signTeacherService.addSingScore(addSignScoreDto);
    }

    @ApiOperation(value = "成员管理")
    @RequestMapping(value = "/manageStudent", method = RequestMethod.POST)
    public MData manageStudent(@RequestBody @Validated SignDetailDto signDetailDto){
        return signTeacherService.manageStudent(signDetailDto);
    }

    @ApiIgnore(value = "删除学生")
    @RequestMapping(value = "/deleteStudent", method = RequestMethod.POST)
    public MData deleteStudent(@RequestBody @Validated SignClassUserDto deleteStudentDto){
        return signTeacherService.deleteStudent(deleteStudentDto);
    }

    @ApiOperation(value = "某学生签到详情")
    @RequestMapping(value = "/userSignDetail", method = RequestMethod.POST)
    public MData userSignDetail(@RequestBody @Validated SignClassUserDto deleteStudentDto){
        return signTeacherService.userSignDetail(deleteStudentDto);
    }
}
