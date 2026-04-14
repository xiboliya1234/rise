package com.rise.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName("user")
public class userModel {

    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("username")
    private String username;
    @TableField("password")
    private String password;
    @TableField("status")
    private Integer status;
    // 0 未激活 1 激活 2 锁定
    @TableField(exist = false)
    private String token;
}
