package com.rise.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.github.pagehelper.PageInfo;
import com.rise.model.dzModel;
import com.rise.model.sblxModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
public interface DzDao extends BaseMapper<dzModel> {

   // @Select("select * from dz")
    List<dzModel> getDz();
    // 这里应该包含对数据库的增删改查操作

    @Select("select concat(lx,',',count(sbmc) ) as qwe from dz GROUP BY lx")
    String[] getDzbie();

    List<dzModel> getDataBysbmc(@Param("params") dzModel params);






    List<sblxModel> getsblx(@Param("params") sblxModel params);
}
