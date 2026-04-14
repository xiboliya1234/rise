package com.rise.mapper;

import java.util.List;

import com.rise.model.ExcelModel;

public class ExcelDao {
	 public void save(List<ExcelModel> list) {
	        // 如果是mybatis,尽量别直接调用多次insert,自己写一个mapper里面新增一个方法batchInsert,所有数据一次性插入
	    }
}
