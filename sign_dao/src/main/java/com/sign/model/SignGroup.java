package com.sign.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author MM
 * @description
 * @create 2020-04-22 16:03
 **/
@Data
@TableName("sign_group")
public class SignGroup extends BaseModel{
    private Integer id;
    private String signType;
}
