package com.sign.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author MM
 * @description
 * @create 2020-04-22 15:49
 **/
@Data
@TableName(value = "user")
public class User extends BaseModel{
//    @TableId(type = IdType.AUTO)
//    private Integer id;
    private String avatarUrl;
    private String nickName;
    private String openId;
    private String telephone;
}
