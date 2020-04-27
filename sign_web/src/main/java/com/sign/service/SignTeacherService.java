package com.sign.service;

import com.sign.dao.SignClassDao;
import com.sign.dao.SignClassUserDao;
import com.sign.model.SignClass;
import com.sign.model.SignClassUser;
import com.sign.pojo.SignClassUserDto;
import com.sign.pojo.SignDetailDto;
import com.sign.pojo.SignTaskDto;
import com.sign.util.MData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author MM
 * @description
 * @create 2020-04-27 15:23
 **/
@Slf4j
@Service
public class SignTeacherService {

    @Autowired
    private SignUserService signUserService;

    @Autowired
    private SignClassUserDao signClassUserDao;

    @Autowired
    private SignClassDao signClassDao;

    public MData createSignTask(SignTaskDto signTaskDto) {
        //需要先判断是否有未签到


        return new MData();
    }

    public MData deleteStudent(SignClassUserDto deleteStudentDto) {
        return new MData();
    }

    public MData userSignDetail(SignClassUserDto deleteStudentDto) {
        Integer classUserId = deleteStudentDto.getClassUserId();
        MData result = new MData();
        SignClassUser classUser = signClassUserDao.selectById(classUserId);
        if (classUser == null) {
            log.info("userSignDetail:{}", classUserId);
            return result.error("classUserId is error");
        }
        SignClass signClass = signClassDao.selectById(classUser.getClassId());

        SignDetailDto signDetailDto = new SignDetailDto();
        signDetailDto.setClassId(classUser.getClassId());
        signDetailDto.setUid(classUser.getUid());

        Map<String, Object> data = signUserService.scoreDetail(signDetailDto);

        data.put("className", signClass.getClassName());
        result.setData(data);

        return result;
    }

    public MData manageStudent(SignDetailDto signDetailDto) {
        MData result = new MData();
        List<SignClassUser> classUserList = signClassUserDao.queryClassUserByClassId(signDetailDto.getClassId());

        result.setData(classUserList);
        return result;
    }
}
