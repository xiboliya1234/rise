package com.rise.model;

import cn.hutool.core.annotation.Alias;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@TableName("sblx")
public class sblxModel {
    @TableId(type = IdType.AUTO)
    private Integer id;
    @Alias("类型名称")
    @TableField("lxmc")
    private String lxmc;
    @TableField("lxid")
    private String lxid;
    @TableField(exist = false)
    private int pagenum;
    @TableField(exist = false)
    private int pagesize;
}
