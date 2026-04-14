package com.rise.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rise.model.userModel;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;

@Mapper
public interface UserDao extends BaseMapper<userModel> {




}
