package com.sign.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author MM
 * @description
 * @create 2020-04-26 18:30
 **/
@Data
public class UserSignDto {
    @Positive(message = "uid 必须大于0")
    private Integer uid;
    @NotNull(message = "taskId不能为空")
    private Integer taskId;

    private Double latitude;
    private Double longitude;
}
