package com.rise.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@TableName("video_path_prefix")
public class videoPathPrefixModel {

    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField("prefix_name")
    private String prefixName;

    @TableField("prefix_value")
    private String prefixValue;

    @TableField("sort_num")
    private Integer sortNum;

    @TableField("enabled")
    private Integer enabled;

    @TableField("remark")
    private String remark;
}

