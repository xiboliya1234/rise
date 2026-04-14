package com.rise.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rise.dao.CslxDao;
import com.rise.dao.DzDao;
import com.rise.exception.CustomException;
import com.rise.model.dzModel;
import com.rise.model.sblxModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DzService
{
    @Resource
    private DzDao dzDao;

    @Resource
    private CslxDao cd;

    public List<dzModel> getDz(){


       // return dzDao.getDz();
        return dzDao.getDz();

    }

    public List<Map<String, Object>>getDzbie(){

        List<Map<String, Object>> mapList = new ArrayList<>();
        String qwe []=dzDao.getDzbie();
        for(int i=0;i<qwe.length;i++){
            Map<String, Object> map = new HashMap<>();
            map.put("name", qwe[i].split(",")[0]);
            map.put("value",qwe[i].split(",")[1]);
            mapList.add(i,map);
        }
        // return dzDao.getDz();
        return mapList;

    }



    public PageInfo<dzModel> getDataBysbmc(dzModel params){
        PageHelper.startPage(params.getPagenum(),params.getPagesize());
        List<dzModel> dz = dzDao.getDataBysbmc(params);
        //System.out.println("进入查询地址服务service:"+params.getSbmc());
        // return dzDao.getDz();
        return PageInfo.of(dz);

    }
    public List<dzModel> getDataBysbmc2(){
        QueryWrapper<dzModel> queryWrapper = new QueryWrapper();
        List<dzModel> dz = dzDao.selectList(queryWrapper);
        return (dz);
    }

    public PageInfo<sblxModel> getsblx(sblxModel params){
        PageHelper.startPage(params.getPagenum(),params.getPagesize());
        List<sblxModel> lx = dzDao.getsblx(params);
        //System.out.println("进入查询地址服务service:"+params.getSbmc());
        // return dzDao.getDz();
        return PageInfo.of(lx);

    }

    public void savedz(dzModel params) {
        if(StrUtil.isEmpty(params.getLxid())){
            throw new CustomException("场所类型未填写");
        }

        if(StrUtil.isEmpty(params.getSbmc())){
            throw new CustomException("场所名称未填写");
        }

        QueryWrapper<sblxModel> queryWrapper = new QueryWrapper();
        queryWrapper.eq("id", params.getLxid());//tom_age必须是数据库中的字段
        sblxModel sb=cd.selectOne(queryWrapper);

        params.setLx(sb.getLxmc());

        dzDao.insert(params);
    }

    public void updatedz(@Param("et") dzModel params) {

        dzDao.updateById(params);
    }

    public void deletedz(Integer id) {
        dzDao.deleteById(id);
    }
}
