package com.sign.pojo;

import lombok.Data;

import javax.validation.constraints.Positive;

/**
 * @author MM
 * @description
 * @create 2020-04-26 14:51
 **/
@Data
public class SignUserDto {
    @Positive(message = "uid不能为空")
    private Integer uid;
}
