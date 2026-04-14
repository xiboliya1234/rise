package com.rise.service;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rise.dao.videoDao;
import com.rise.model.dzModel;
import com.rise.model.videoinfoModel;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class videoService {

    @Resource
    videoDao vd;

    public PageInfo<videoinfoModel> getDataBysbmc(videoinfoModel params){

        PageHelper.startPage(params.getPagenum(),params.getPagesize());
        List<videoinfoModel> vm = vd.getVideoinfo(params);
        //System.out.println("进入查询地址服务service:"+params.getSbmc());
        // return dzDao.getDz();
        return PageInfo.of(vm);

    }

    public void savevideo(videoinfoModel params) {
        vd.insert(params);
    }

    public void updatevideo(@Param("et") videoinfoModel params) {

        vd.updateById(params);
    }

    public void deletevideo(Integer id) {
        vd.deleteById(id);
    }
}
