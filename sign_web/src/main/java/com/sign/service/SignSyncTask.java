package com.sign.service;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sign.dao.SignClassDao;
import com.sign.dao.SignClassUserDao;
import com.sign.dao.UserDao;
import com.sign.model.BaseModel;
import com.sign.model.SignClass;
import com.sign.model.SignClassUser;
import com.sign.model.User;
import com.sign.util.Constants;
import com.sign.util.MData;
import com.sign.util.MpCompentent;
import com.sign.util.MpWxAccessToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Component
public class SignSyncTask {

    @Autowired
    private SignClassDao signClassDao;


    @Autowired
    private UserDao userDao;

    @Autowired
    private SignClassUserDao signClassUserDao;

    /**
     * 每天固定时间
     */
    @Scheduled(cron = "0 0 8 * * ?")
//    @Scheduled(cron = "0/4 * * * * ? ")
    public void checkDesignerAccount() {
        log.info("检查课时数 过半 通知 current date: {}", DateUtil.now());

        QueryWrapper<SignClass> wrapper = new QueryWrapper<>();
        wrapper.eq("status", Constants.CLASS_ACTIVE);
        List<SignClass> signClassesList = signClassDao.selectList(wrapper);
        Map<Integer, SignClass> classIds = new HashMap<>();
        for (SignClass signClass : signClassesList) {
            Integer half = signClass.getClassHour() / 2;
            if(signClass.getNotifyStatus().equals(1)){
                continue;
            }
            if (signClass.getConsumeHour().compareTo(half) >= 0) {
                classIds.put(signClass.getId(), signClass);
            }
        }
        if (classIds.isEmpty()) {
            return;
        }
        QueryWrapper<SignClassUser> wrapperUser = new QueryWrapper<>();
        wrapperUser.in("class_id", classIds.keySet());
        List<SignClassUser> classUsers = signClassUserDao.selectList(wrapperUser);
        Map<Integer, List<SignClassUser>> classUserMap = classUsers.stream()
                .collect(Collectors.groupingBy(SignClassUser::getClassId));
        for (Map.Entry<Integer, List<SignClassUser>> integerListEntry : classUserMap.entrySet()) {
            Integer classId = integerListEntry.getKey();
            List<SignClassUser> classUserList = integerListEntry.getValue();
            classUserList.sort(Comparator.comparing(SignClassUser::getScore));
            int size = (int) Math.round(classUserList.size() * 0.1);
            size = size == 0 ? 1 : size;
            List<SignClassUser> userList = classUserList.subList(0, size);
            Map<Integer, SignClassUser> userMap = userList.stream().collect(Collectors.toMap(SignClassUser::getUid, t -> t));
            //班级信息
            SignClass signClass = classIds.get(classId);
            List<User> users = userDao.selectBatchIds(new ArrayList<>(userMap.keySet()));
            //发送消息
            MpWxAccessToken accessToken = MpCompentent.getAccessToken(Constants.APPID, Constants.APP_SECRET);
            for (User user : users) {
                SignClassUser classUser = userMap.get(user.getId());
                log.info("send: {}", classUser.getUid());
                MData result = MpCompentent.sendScoreToOpenId(accessToken.getAccessToken(), user.getOpenId(),
                        "积分排名低", signClass.getConsumeHour(), classUser.getStudentName(), signClass.getClassName());
                if(result.get("status").equals("ok")){
                    SignClass signClassUpdate = new SignClass();
                    signClassUpdate.setId(classId);
                    signClassUpdate.setNotifyStatus(1);
                    signClassDao.updateById(signClassUpdate);
                }
            }
        }
    }
}
