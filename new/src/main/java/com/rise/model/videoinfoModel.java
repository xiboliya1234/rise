package com.rise.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName("videoinfo")
public class videoinfoModel {


    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("videoname")
    private String videoname;
    @TableField("videocode")
    private String videocode;
    @TableField("videomsg")
    private String videomsg;
    @TableField("videodate")
    private String videodate;
    @TableField("videoactor")
    private String videoactor;
    @TableField("videoimg")
    private String videoimg;
    @TableField("videotag")
    private String videotag;
    @TableField("videourl")
    private String videourl;
    private Integer pagenum;

    private Integer pagesize;
}
