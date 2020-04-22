package com.sign.service;

import com.sign.dao.UserDao;
import com.sign.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户业务相关
 *
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

    public User queryUserByOpenId(String openId) {
        return userDao.queryUserByOpenId(openId);
    }

    public int addUser(User user) {
        return userDao.insert(user);
    }

    public void updateUser(User user) {
        userDao.updateById(user);
    }

    /**
     * 检查用户是否存在，不存在则创建
     *
     * @param openid
     * @return
     */
    public User checkUserIsExist(String openid) {
        User user = queryUserByOpenId(openid);
        if (user == null) {
            user = new User();
            user.setOpenId(openid);
            addUser(user);
        }
        return user;
    }


}
