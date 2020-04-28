package com.sign.pojo;

import lombok.Data;

/**
 * @author MM
 * @description
 * @create 2020-04-26 14:55
 **/
@Data
public class SignIndexVo {

    private Integer classId;
    private String className;
    private String signName;
    private String teacherName;

    private Boolean isCreated;

}
