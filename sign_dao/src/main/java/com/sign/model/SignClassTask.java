package com.sign.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author MM
 * @description
 * @create 2020-04-26 14:47
 **/
@Data
@TableName("sign_class_task")
public class SignClassTask extends BaseModel{

    private Integer classId;
    private String taskType;

    private Date startTime;
    private Date endTime;
}
