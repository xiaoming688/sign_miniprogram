package com.sign.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author MM
 * @description
 * @create 2020-04-26 16:03
 **/
@Data
public class SignDetailDto {
    @Positive(message = "uid不能为空")
    private Integer uid;
    @Positive(message = "classId不能为空")
    private Integer classId;

//    private Integer taskId;
}
