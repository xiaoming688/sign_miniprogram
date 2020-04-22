package com.sign.service;

import com.sign.dao.UserDao;
import com.sign.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户业务相关
 * @author MM
 * @description
 * @create 2020-04-22 16:08
 **/
@Service
public class UserService {
    @Autowired
    private UserDao userDao;


    public User queryUserById(Integer userId) {
        return userDao.queryUserById(userId);
    }
}
