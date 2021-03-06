package com.sign.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author MM
 * @description 班级签到记录
 * @create 2020-04-22 16:00
 **/
@Data
@TableName("sign_class_record")
public class SignClassRecord extends BaseModel {
    private Integer classId;
    private Integer taskId;
    private Integer uid;
    private Double latitude;
    private Double longitude;
    private Integer score;
}
