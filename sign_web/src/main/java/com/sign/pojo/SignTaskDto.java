package com.sign.pojo;

import lombok.Data;

import java.util.Date;

/**
 * @author MM
 * @description
 * @create 2020-04-27 15:26
 **/

@Data
public class SignTaskDto {
    private Integer classId;
    private Integer uid;

    private Date startTime;
    private Date endTime;
}
