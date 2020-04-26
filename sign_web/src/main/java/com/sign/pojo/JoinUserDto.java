package com.sign.pojo;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author MM
 * @description
 * @create 2020-04-26 15:37
 **/
@Data
public class JoinUserDto {
    @NotNull(message = "uid不能为空")
    private Integer uid;
    private String userNo;
    private String studentName;
    private Integer classId;
}
