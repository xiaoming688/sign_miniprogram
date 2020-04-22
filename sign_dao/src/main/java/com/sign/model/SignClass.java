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
    private Integer signId;
    private Integer createUserId;
    private String introduce;
    private String telephone;
    private String className;
    private Date startTime;
    private Date endTime;
    private Double latitude;
    private Double longitude;
    private Integer limitArea;
    private String status;
}
