package com.rise.controller;


import com.github.pagehelper.PageInfo;
import com.rise.common.Result;
import com.rise.model.dzModel;
import com.rise.model.videoinfoModel;
import com.rise.service.videoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/vue/video")
public class videoController {

    @Autowired
    videoService vs;

    private static final Logger logger = LoggerFactory.getLogger(videoController.class);

    @RequestMapping("/getVideo")
    public Result getVideo(videoinfoModel params){
        logger.info("进入查询视频服务:"+params.getPagesize());

        PageInfo<videoinfoModel> dz = vs.getDataBysbmc(params);
        //System.out.println("进入查询地址服务:"+dz.toString());
        return Result.success(dz);
    }

    @RequestMapping("/saveVideo")
    public Result savevideo(@RequestBody videoinfoModel params){
        logger.info("进入新增场所类型:"+params.getId());
        if(params.getId()!=null){
            vs.updatevideo(params);
            return Result.success();
        }else{
            vs.savevideo(params);

            return Result.success();
        }
    }
    @DeleteMapping("/{id}")
    public Result deletevideo(@PathVariable Integer id){
        logger.info("进入删除地址:"+id);
        vs.deletevideo(id);
        return Result.success();
    }


}
