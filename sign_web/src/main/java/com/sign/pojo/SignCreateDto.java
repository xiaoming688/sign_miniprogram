package com.sign.pojo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Date;

/**
 * @author MM
 * @description
 * @create 2020-04-26 15:10
 **/
@Data
public class SignCreateDto {

    @Positive(message = "uid不能为空")
    private Integer uid;
    @NotEmpty(message = "className不能为空")
    private String className;
    @NotEmpty(message = "signName不能为空")
    private String signName;
    @NotNull(message = "classHour不能为空")
    private Integer classHour;
    private String introduce;

    private String teacherName;

    private String location;
    private Date startTime;
    private Date endTime;
    private Integer limitNumber;
    private Double latitude;
    private Double longitude;
    private Integer limitArea;
    private Integer score;
}
