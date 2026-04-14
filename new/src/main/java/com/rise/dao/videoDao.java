package com.rise.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rise.model.dzModel;
import com.rise.model.videoinfoModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface videoDao extends BaseMapper<videoinfoModel> {


    List<videoinfoModel> getVideoinfo(@Param("params") videoinfoModel params);
}
