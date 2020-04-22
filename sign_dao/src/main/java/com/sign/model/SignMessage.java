package com.sign.model;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 消息日志
 * @author MM
 * @description
 * @create 2020-04-22 16:25
 **/
@Data
@TableName("sign_message")
public class SignMessage extends BaseModel{
    private Integer toUserId;
    private String message;
    private String status;
}
