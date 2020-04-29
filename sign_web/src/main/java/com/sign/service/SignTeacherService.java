package com.sign.service;

import com.sign.dao.SignClassDao;
import com.sign.dao.SignClassTaskDao;
import com.sign.dao.SignClassTaskRecordDao;
import com.sign.dao.SignClassUserDao;
import com.sign.model.SignClass;
import com.sign.model.SignClassRecord;
import com.sign.model.SignClassTask;
import com.sign.model.SignClassUser;
import com.sign.pojo.*;
import com.sign.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
    private SignClassTaskDao signClassTaskDao;

    @Autowired
    private SignClassTaskRecordDao signClassTaskRecordDao;

    @Autowired
    private SignClassDao signClassDao;

    public MData createSignTask(SignTaskDto signTaskDto) {
        MData result = new MData();
        //需要先判断是否有未签到
        Integer classId = signTaskDto.getClassId();
        //是否有未签到任务。。。
        List<SignClassTask> signTasks = signClassTaskDao.queryTaskByClassId(classId);
        Date now = new Date();
        SignClassTask signClassTask = null;
        for (SignClassTask signTask : signTasks) {
            //只需要签到的task
            if (!signTask.getTaskType().equals(Constants.TASK_TYPE_SIGN)) {
                continue;
            }
            //时间在期间
            if (DateUtil.belongCalendar(now, signTask.getStartTime(), signTask.getEndTime())) {
                signClassTask = signTask;
                break;
            }
        }
        if (signClassTask != null) {
            return result.error("目前存在未完成签到，请签到结束后再试~");
        }
        log.info("signTaskDto:{}", signTaskDto);
        Date startTime = cn.hutool.core.date.DateUtil.parse(signTaskDto.getStartTime());
        Date endTime = cn.hutool.core.date.DateUtil.parse(signTaskDto.getEndTime());

        if (startTime.after(endTime)) {
            return result.error("起始日期不正确");
        }
        SignClassTask task = new SignClassTask();
        task.setClassId(classId);
        task.setStartTime(startTime);
        task.setEndTime(endTime);
        task.setTaskType(Constants.TASK_TYPE_SIGN);
        signClassTaskDao.insert(task);
        //todo 发通知
        List<SignClassUserVo> classUserList = signClassUserDao.queryClassUserBasicByClassId(classId);
        SignClass signClass = signClassDao.selectById(classId);
        String timeStr = cn.hutool.core.date.DateUtil.format(startTime, "yyyy-MM-dd HH:mm")
                + "~" + cn.hutool.core.date.DateUtil.format(endTime, "HH:mm");

        MpWxAccessToken accessToken = MpCompentent.getAccessToken(Constants.APPID, Constants.APP_SECRET);
        for (SignClassUserVo classUser : classUserList) {
            MpCompentent.sendMessageToOpenId(accessToken.getAccessToken(), classUser.getOpenId(),
                    timeStr, signClass.getClassName(), signClass.getTeacherName(), signClass.getSignName());
        }
        result.put("taskId", task.getId());
        return result;
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

    @Transactional
    public MData addSingScore(AddSignScoreDto addSignScoreDto) {
        MData result = new MData();
        Integer classUserId = addSignScoreDto.getClassUserId();
        Integer score = addSignScoreDto.getScore();
        SignClassUser classUser = signClassUserDao.selectById(classUserId);
        if (classUser == null) {
            log.error("classUserId is error {}", classUserId);
            return result.error("classUserId is error");
        }
        if (score == null || score < 0) {
            log.error("score is error {}", classUserId);
            return result.error("score is error");
        }

        classUser.setScore(classUser.getScore() + score);
        signClassUserDao.updateById(classUser);

        SignClass signClass = signClassDao.selectById(classUser.getClassId());

        SignClassTask task = new SignClassTask();
        task.setTaskType(Constants.TASK_TYPE_CHOOSE);
        task.setClassId(classUser.getClassId());
        signClassTaskDao.insert(task);

        SignClassRecord record = new SignClassRecord();
        record.setClassId(classUser.getClassId());
        record.setTaskId(task.getId());
        record.setLatitude(signClass.getLatitude());
        record.setLongitude(signClass.getLongitude());
        record.setScore(score);
        record.setUid(classUser.getUid());
        signClassTaskRecordDao.insert(record);
        return result;

    }

    public MData studentList(SignDetailDto signDetailDto) {
        MData result = new MData();
        Map<Integer, SignClassRecord> signUsers = new HashMap<>();
        Integer classId = signDetailDto.getClassId();
        //是否有未签到任务。。。
        List<SignClassTask> signTasks = signClassTaskDao.queryTaskByClassId(classId);
        Date now = new Date();
        SignClassTask signClassTask = null;
        for (SignClassTask signTask : signTasks) {
            //只需要签到的task
            if (!signTask.getTaskType().equals(Constants.TASK_TYPE_SIGN)) {
                continue;
            }
            //时间在期间
            if (DateUtil.belongCalendar(now, signTask.getStartTime(), signTask.getEndTime())) {
                signClassTask = signTask;
                break;
            }
        }
        if (signClassTask != null) {
            List<SignClassRecord> classTaskList = signClassTaskRecordDao.queryRecordByTaskId(classId, signClassTask.getId());
            signUsers = classTaskList.stream().collect(Collectors.toMap(SignClassRecord::getUid, t -> t));
        }
        List<SignClassUser> classUserList = signClassUserDao.queryClassUserByClassId(classId);

        List<Map<String, Object>> userList = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        for (SignClassUser signClassUser : classUserList) {
            Map<String, Object> user = new HashMap<>();
            user.put("classUserId", signClassUser.getId());
            user.put("name", signClassUser.getStudentName());

            if (signUsers.containsKey(signClassUser.getUid())) {
                userList.add(user);
            }
        }
        data.put("userList", userList);
        data.put("canSignTaskId", signClassTask == null ? 0 : signClassTask.getId());
        data.put("signDate", signClassTask == null ? "" : cn.hutool.core.date.DateUtil.format(signClassTask.getStartTime(), "yyyy-MM-dd"));
        data.put("signStartTime", signClassTask == null ? "" : cn.hutool.core.date.DateUtil.format(signClassTask.getStartTime(), "HH:mm"));
        data.put("signEndTime", signClassTask == null ? "" : cn.hutool.core.date.DateUtil.format(signClassTask.getEndTime(), "HH:mm"));

        result.setData(data);
        return result;
    }
}
