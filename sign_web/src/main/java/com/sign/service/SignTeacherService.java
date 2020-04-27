package com.sign.service;

import com.sign.pojo.SignClassUserDto;
import com.sign.pojo.SignTaskDto;
import com.sign.util.MData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author MM
 * @description
 * @create 2020-04-27 15:23
 **/
@Slf4j
@Service
public class SignTeacherService {

    public MData createSignTask(SignTaskDto signTaskDto) {
        //需要先判断是否有未签到


        return new MData();
    }

    public MData deleteStudent(SignClassUserDto deleteStudentDto) {
        return new MData();
    }

    public MData userSignDetail(SignClassUserDto deleteStudentDto) {
        return new MData();
    }
}
