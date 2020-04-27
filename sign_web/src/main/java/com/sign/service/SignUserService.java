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
        List<SignIndexVo> indexVoList = signClassUserDao.querySignIndexVo(userId);
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
        if (classUser == null) {
            data.put("isJoin", false);
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
        List<Integer> signUsers = new ArrayList<>();
        if (signClassTask != null) {
            List<SignClassRecord> classTaskList = signClassTaskRecordDao.queryRecordByTaskId(classId, signClassTask.getId());
            signUsers = classTaskList.stream().map(SignClassRecord::getUid).collect(Collectors.toList());
        }

        List<SignClassUser> classUserList = signClassUserDao.queryClassUserByClassId(classId);

        Boolean currentUser = false;
        List<Map<String, Object>> userList = new ArrayList<>();
        for (SignClassUser signClassUser : classUserList) {
            Map<String, Object> user = new HashMap<>();
            user.put("classUserId", signClassUser.getId());
            user.put("userNo", signClassUser.getUserNo());
            user.put("studentName", signClassUser.getStudentName());
            user.put("score", signClassUser.getScore().toString());
            user.put("isSigned", signUsers.contains(signClassUser.getUid()));

            if (signClassUser.getUid().equals(uid)) {
                currentUser = signUsers.contains(signClassUser.getUid());
            }
            userList.add(user);
        }

        data.put("userList", userList);
        data.put("canSignTaskId", signClassTask == null ? 0 : signClassTask.getId());
        data.put("isSign", currentUser);

        data.put("isTeacher", uid.equals(signClass.getUid()));
        data.put("isJoin", true);

        data.put("signCreate", signClass.getSignName());
        data.put("signIntroduce", signClass.getIntroduce());

        data.put("alertScore", true);

        result.setData(data);
        return result;
    }

    public Double getLocationDouble(Object value) {
        return StringUtil.isBlank(value) ? Double.valueOf(0) : Double.valueOf(String.valueOf(value));
    }

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
        return result;
    }

    /**
     * 加分记录
     *
     * @param signDetailDto
     * @return
     */
    public MData scoreDetail(SignDetailDto signDetailDto) {
        MData result = new MData();
        Integer uid = signDetailDto.getUid();
        Integer classId = signDetailDto.getClassId();

        List<SignClassRecord> recordList = signClassTaskRecordDao.queryUserRecordByClassId(classId, uid);


        return result;
    }

    /**
     * 积分排行
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
