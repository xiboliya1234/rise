package com.rise.controller;

import com.github.pagehelper.PageInfo;
import com.rise.common.Result;
import com.rise.model.dzModel;
import com.rise.model.mapmodel;
import com.rise.service.DzService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/vue/map")
public class MapController {
    private static final Logger logger = LoggerFactory.getLogger(MapController.class);
    @Autowired
    DzService ds;
    @RequestMapping("/getData")
    public Result getData(dzModel params){


        List<dzModel> dz = ds.getDz();
        List<mapmodel> map =new ArrayList<>();
        System.out.println("进入查询地址服务:"+dz.toString());
        for (int i = 0; i < dz.size(); i++) {
            dzModel currentDz = dz.get(i);
            map.add(new mapmodel(currentDz.getSbmc(), currentDz.getX(), currentDz.getY(),currentDz.getLx()));
        }
        System.out.println("进入查询地址服务:"+dz.toString());
        return Result.success(map);
    }
}

