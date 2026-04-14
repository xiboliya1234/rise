package com.rise.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;


@Setter
@Getter
@TableName("dz")
public class dzModel {

    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("x")
    private String x;
    @TableField("y")
    private String y;
    @TableField("lx")
    private String lx;
    @TableField("s")
    private String s;
    @TableField("q")
    private String q;
    @TableField("jd")
    private String jd;
    @TableField("sbmc")
    private String sbmc;
    @TableField("xz")
    private String xz;

    @TableField("lxid")
    private String lxid;
    @TableField(exist = false)
    private Integer pagenum;
    @TableField(exist = false)
    private Integer pagesize;
}
