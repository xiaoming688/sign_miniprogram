package com.sign.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author MM
 * @description
 * @create 2020-04-22 15:51
 **/
@Data
@TableName("sign_class")
public class SignClass extends BaseModel{
    private Integer uid;
    private Integer classHour;
    private Integer consumeHour;

    private String introduce;
    private String telephone;
    private String className;

    private String teacherName;
    private String signName;

    private Date startTime;
    private Date endTime;

    private Double latitude;
    private Double longitude;

    private String signLocation;
    private Integer limitArea;
    private Integer limitNumber;
    private Integer score;

    private String status;

    private Integer notifyStatus;
}
