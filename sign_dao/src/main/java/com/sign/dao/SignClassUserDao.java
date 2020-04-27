package com.sign.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sign.model.SignClassUser;
import com.sign.model.User;
import com.sign.pojo.SignIndexVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author MM
 * @description
 * @create 2020-04-26 14:57
 **/
@Mapper
@Repository
public interface SignClassUserDao extends BaseMapper<SignClassUser> {

    @Select("select c.id as classId, c.class_name as className, c.teacher_name as teacherName, c.sign_name as signName from sign_class_user l " +
            "left join sign_class c on c.id = l.class_id where l.uid = #{userId} and c.status='active'")
    List<SignIndexVo> querySignIndexVo(@Param("userId") Integer userId);


    @Select("SELECT * FROM sign_class_user WHERE uid = #{uid} and class_id=#{classId}")
    SignClassUser queryClassUserById(@Param("uid") Integer uid, @Param("classId") Integer classId);


    @Select("SELECT * FROM sign_class_user WHERE class_id=#{classId} order by score desc")
    List<SignClassUser> queryClassUserByClassId(@Param("classId") Integer classId);
}
