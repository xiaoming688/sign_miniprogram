package com.sign.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sign.model.SignClassRecord;
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
 * @create 2020-04-26 17:31
 **/
@Mapper
@Repository
public interface SignClassTaskRecordDao extends BaseMapper<SignClassRecord> {
    @Select("SELECT * FROM sign_class_record WHERE class_id=#{classId}  and task_id=#{taskId}")
    List<SignClassRecord> queryRecordByTaskId(@Param("classId") Integer classId, @Param("taskId") Integer taskId);

    @Select("SELECT * FROM sign_class_record WHERE class_id=#{classId}")
    List<SignClassRecord> queryRecordByClassId(@Param("classId") Integer classId);

    @Select("SELECT * FROM sign_class_record WHERE class_id=#{classId}  and uid=#{uid}")
    List<SignClassRecord> queryUserRecordByClassId(@Param("classId") Integer classId, @Param("uid") Integer uid);

    @Select("SELECT * FROM sign_class_record WHERE uid=#{uid}  and task_id=#{taskId}")
    SignClassRecord queryUserRecordByTaskId(@Param("uid") Integer uid, @Param("taskId") Integer taskId);

}
