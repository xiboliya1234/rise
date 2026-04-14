package com.rise.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName("file")
public class fileModel {

    @TableId(type = IdType.AUTO)
    private Integer id;
    @TableField("filename")
    private String filename;
    @TableField("filecode")
    private String filecode;
    @TableField("createdate")
    private String createdate;
    @TableField("filepath")
    private String filepath;
    @TableField("downpath")
    private String downpath;


}
