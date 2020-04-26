package com.sign.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author MM
 * @description
 * @create 2020-04-26 14:45
 **/
@Data
@TableName("sign_class_user")
public class SignClassUser extends BaseModel{

    private Integer classId;

    private Integer uid;

    private String studentName;

    private String userNo;

    private Integer score;
}
