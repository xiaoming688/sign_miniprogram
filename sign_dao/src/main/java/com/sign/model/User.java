package com.sign.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author MM
 * @description
 * @create 2020-04-22 15:49
 **/
@Data
@TableName(value = "user")
public class User extends BaseModel{
    private Integer id;
    private String headImg;
    private String userName;
    private String openId;
    private String telephone;
}
