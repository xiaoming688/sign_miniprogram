package com.sign.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sign.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author MM
 * @description
 * @create 2020-04-22 16:06
 **/
@Mapper
@Repository
public interface UserDao extends BaseMapper<User> {
    @Select("SELECT * FROM user WHERE id = #{id}")
    User queryUserById(@Param("id") Integer id);
    @Select("SELECT * FROM user WHERE open_id = #{openId}")
    User queryUserByOpenId(@Param("openId") String openId);
}
