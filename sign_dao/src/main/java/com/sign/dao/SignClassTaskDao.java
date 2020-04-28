package com.sign.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sign.model.SignClassTask;
import com.sign.model.SignClassUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author MM
 * @description
 * @create 2020-04-26 16:34
 **/
@Mapper
@Repository
public interface SignClassTaskDao extends BaseMapper<SignClassTask> {
    @Select("SELECT * FROM sign_class_task WHERE class_id=#{classId} and task_type='sign'")
    List<SignClassTask> queryTaskByClassId(@Param("classId") Integer classId);
}
