package com.sign.service;

import com.sign.dao.SignClassDao;
import com.sign.dao.SignClassTaskDao;
import com.sign.dao.SignClassTaskRecordDao;
import com.sign.dao.SignClassUserDao;
import com.sign.model.*;
import com.sign.pojo.*;
import com.sign.util.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author MM
 * @description
 * @create 2020-04-26 14:53
 **/
@Slf4j
@Service
public class SignUserService {

    @Autowired
    private SignClassUserDao signClassUserDao;
    @Autowired
    private SignClassDao signClassDao;
    @Autowired
    private SignClassTaskDao signClassTaskDao;

    @Autowired
    private SignClassTaskRecordDao signClassTaskRecordDao;

    /**
     * 签到首页数据
     *
     * @param signUserDto
     * @return
     */
    public MData getIndex(SignUserDto signUserDto) {
        MData result = new MData();
        Integer userId = signUserDto.getUid();
        //加入的班级
        List<SignIndexVo> indexVoList = signClassUserDao.querySignIndexVo(userId);
        for (SignIndexVo signIndexVo : indexVoList) {
            signIndexVo.setIsCreated(false);
        }
        //创建的班级
        List<SignIndexVo> createVoList = signClassUserDao.querySignClassIndexVo(userId);
        for (SignIndexVo signIndexVo : createVoList) {
            signIndexVo.setIsCreated(true);
        }
        indexVoList.addAll(createVoList);

        result.setData(indexVoList);
        return result;
    }

    /**
     * 创建签到
     *
     * @param signUserDto
     * @return
     */
    public MData createSign(SignCreateDto signUserDto) {
        MData result = new MData();
        SignClass signClass = new SignClass();
        signClass.setClassName(signUserDto.getClassName());
        signClass.setUid(signUserDto.getUid());

        signClass.setTeacherName(signUserDto.getTeacherName());

        signClass.setClassHour(signUserDto.getClassHour());
        signClass.setIntroduce(signUserDto.getIntroduce());
        signClass.setSignName(signUserDto.getSignName());

        signClass.setLatitude(signUserDto.getLatitude());
        signClass.setLongitude(signUserDto.getLongitude());

        signClass.setLimitArea(signUserDto.getLimitArea());
        signClass.setLimitNumber(signUserDto.getLimitNumber());

        signClass.setStartTime(signUserDto.getStartTime());
        signClass.setEndTime(signUserDto.getEndTime());
        signClass.setScore(signUserDto.getScore());

        signClass.setStatus(Constants.CLASS_ACTIVE);

        signClassDao.insert(signClass);

        return result;
    }

    /**
     * 加入班级
     *
     * @param joinUserDto
     * @return
     */
    public MData joinClass(JoinUserDto joinUserDto) {

        MData result = new MData();
        Integer uid = joinUserDto.getUid();
        Integer classId = joinUserDto.getClassId();

        SignClassUser classUser = signClassUserDao.queryClassUserById(uid, classId);
        if (classUser != null) {
            return result.error("已加入过该班级");
        }
        SignClassUser signClassUser = new SignClassUser();
        signClassUser.setClassId(classId);
        signClassUser.setUid(uid);
        signClassUser.setScore(0);
        signClassUser.setStudentName(joinUserDto.getStudentName());
        signClassUser.setUserNo(joinUserDto.getUserNo());

        signClassUserDao.insert(signClassUser);

        return result;
    }

    /**
     * 签到详情
     * 1.先判断是否加入过班级
     *
     * @param signDetailDto
     * @return
     */
    public MData signDetail(SignDetailDto signDetailDto) {
        MData result = new MData();
        Integer uid = signDetailDto.getUid();
        Integer classId = signDetailDto.getClassId();
        //先看是否是创建人
        SignClass signClass = signClassDao.selectById(classId);
        if (signClass == null) {
            log.info("signDetail {}", signDetailDto);
            return result.error("classId is error!");
        }
        Map<String, Object> data = new HashMap<>();
        SignClassUser classUser = signClassUserDao.queryClassUserById(uid, classId);
        if (!signClass.getUid().equals(uid) && classUser == null) {
            data.put("isJoin", false);
            data.put("className", signClass.getClassName());
            result.setData(data);
            return result;
        }
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
        Map<Integer, SignClassRecord> signUsers = new HashMap<>();
        if (signClassTask != null) {
            List<SignClassRecord> classTaskList = signClassTaskRecordDao.queryRecordByTaskId(classId, signClassTask.getId());
            signUsers = classTaskList.stream().collect(Collectors.toMap(SignClassRecord::getUid, t -> t));
        }

        List<SignClassUser> classUserList = signClassUserDao.queryClassUserByClassId(classId);

        Boolean currentUser = false;
        List<Map<String, Object>> userList = new ArrayList<>();
        for (SignClassUser signClassUser : classUserList) {
            Map<String, Object> user = new HashMap<>();
            user.put("sId", signClassUser.getId());
            user.put("sNo", signClassUser.getUserNo());
            user.put("name", signClassUser.getStudentName());
            user.put("credit", signClassUser.getScore().toString());

            user.put("signed", false);
            if (signUsers.containsKey(signClassUser.getUid())) {
                user.put("signed", true);
                user.put("latitude", signUsers.get(signClassUser.getUid()).getLatitude());
                user.put("longitude", signUsers.get(signClassUser.getUid()).getLongitude());
            }

            if (signClassUser.getUid().equals(uid)) {
                currentUser = signUsers.containsKey(signClassUser.getUid());
            }
            userList.add(user);
        }

        data.put("userList", userList);
        data.put("canSignTaskId", signClassTask == null ? 0 : signClassTask.getId());
        data.put("signStartTime", signClassTask == null ? "" : cn.hutool.core.date.DateUtil.format(signClassTask.getStartTime(), "HH:mm"));
        data.put("signEndTime", signClassTask == null ? "" : cn.hutool.core.date.DateUtil.format(signClassTask.getEndTime(), "HH:mm"));

        data.put("isSign", currentUser);

        data.put("isCreated", uid.equals(signClass.getUid()));
        data.put("isJoin", true);

        data.put("className", signClass.getClassName());
        data.put("signHour", signClass.getClassHour());
        data.put("signCreate", signClass.getSignName());
        data.put("teacherName", signClass.getTeacherName());
        data.put("signIntroduce", signClass.getIntroduce());

//        data.put("alertScore", true);

        result.setData(data);
        return result;
    }

    public Double getLocationDouble(Object value) {
        return StringUtil.isBlank(value) ? Double.valueOf(0) : Double.valueOf(String.valueOf(value));
    }

    @Transactional
    public MData userSign(UserSignDto signDetailDto) {

        MData result = new MData();
        Integer uid = signDetailDto.getUid();
        Integer taskId = signDetailDto.getTaskId();
        SignClassTask signTask = signClassTaskDao.selectById(taskId);
        if (signTask == null) {
            return result.error("taskId is error");
        }
        SignClassRecord records = signClassTaskRecordDao.queryUserRecordByTaskId(uid, taskId);
        if (records != null) {
            return result.error("该用户已签到");
        }
        SignClassUser signClassUser = signClassUserDao.queryClassUserById(uid, signTask.getClassId());
        if (signClassUser == null) {
            log.info("userSign:{} not join class:{}", uid, signTask.getClassId());
            return result.error("该用户未加入该班级");
        }


        //判断距离是否能签到
        SignClass signClass = signClassDao.selectById(signTask.getClassId());
        Double latitude = getLocationDouble(signDetailDto.getLatitude());
        Double longitude = getLocationDouble(signDetailDto.getLongitude());
        Double distance = MapDistance.getDistance(signClass.getLatitude(), signClass.getLongitude(),
                latitude, longitude);
        BigDecimal distanceKm = BigDecimal.valueOf(distance);
        //km
        BigDecimal limit = BigDecimal.valueOf(signClass.getLimitArea()).multiply(BigDecimal.valueOf(1000));
        if (distanceKm.compareTo(limit) > 0) {
            log.info("distance {} limit {}", distanceKm, limit);
            return result.error("不在签到距离");
        }
        SignClassRecord addRecord = new SignClassRecord();
        addRecord.setLatitude(latitude);
        addRecord.setLongitude(longitude);
        addRecord.setClassId(signClass.getId());
        addRecord.setTaskId(taskId);
        addRecord.setUid(uid);
        addRecord.setScore(signClass.getScore());

        signClassTaskRecordDao.insert(addRecord);


        signClassUser.setScore(signClassUser.getScore() + signClass.getScore());
        signClassUserDao.updateById(signClassUser);

        return result;
    }

    /**
     * 加分记录
     *
     * @param signDetailDto
     * @return
     */
    public Map<String, Object> scoreDetail(SignDetailDto signDetailDto) {
        Integer uid = signDetailDto.getUid();
        Integer classId = signDetailDto.getClassId();
        SignClassUser classUser = signClassUserDao.queryClassUserById(uid, classId);

        List<SignClassRecord> recordList = signClassTaskRecordDao.queryUserRecordByClassId(classId, uid);

        List<SignClassTask> taskList = signClassTaskDao.queryTaskByClassId(classId);
        Map<Integer, String> taskType = new HashMap<>();
        for (SignClassTask signClassTask : taskList) {
            taskType.put(signClassTask.getId(),
                    signClassTask.getTaskType().equals(Constants.TASK_TYPE_SIGN) ? "签到" : "点名");
        }

        List<Map<String, Object>> recordDataList = new ArrayList<>();
        for (SignClassRecord signClassRecord : recordList) {
            Map<String, Object> data = new HashMap<>();
            data.put("recordTime", cn.hutool.core.date.DateUtil.formatDateTime(signClassRecord.getCreateTime()));
            data.put("score", signClassRecord.getScore());
            data.put("scoreType", taskType.get(signClassRecord.getTaskId()));

            recordDataList.add(data);
        }
        Map<String, Object> data = new HashMap<>();
        //老师调用 为空
        if (classUser != null) {
            data.put("studentName", classUser.getStudentName());
        }
        data.put("recordList", recordDataList);

        return data;
    }

    /**
     * 积分排行
     *
     * @param signDetailDto
     * @return
     */
    public MData scoreRank(SignDetailDto signDetailDto) {
        MData result = new MData();
        List<SignClassUser> classUserList = signClassUserDao.queryClassUserByClassId(signDetailDto.getClassId());
        result.setData(classUserList);
        return result;
    }
}
