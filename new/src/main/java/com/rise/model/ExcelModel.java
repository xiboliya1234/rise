package com.rise.model;

import java.util.Date;

import com.alibaba.excel.annotation.ExcelProperty;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class ExcelModel {

	@ExcelProperty("设备id")
    private String id; //设备id
	@ExcelProperty("设备名称")
    private String sbmc;//设备名称
	@ExcelProperty("经度")
    private String x;//经度
	@ExcelProperty("纬度")
    private String y;//纬度
	@ExcelProperty("设备地址")
    private String sbdz; //设备地址
	@ExcelProperty("类型")
    private String lx;//类型

}