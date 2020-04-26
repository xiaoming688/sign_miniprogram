package com.sign.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sign.model.SignClass;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author MM
 * @description
 * @create 2020-04-26 14:56
 **/
@Mapper
@Repository
public interface SignClassDao extends BaseMapper<SignClass> {

}
