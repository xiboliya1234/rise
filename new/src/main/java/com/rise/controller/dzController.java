package com.rise.controller;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rise.FilePathConfig;
import com.rise.common.Result;
import com.rise.exception.CustomException;
import com.rise.model.dzModel;
import com.rise.model.sblxModel;
import com.rise.service.CslxService;
import com.rise.service.DzService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/vue/dz")
public class dzController {
    @Value("${riseout.url}")
    String url;
    @Autowired
    DzService ds;
    @Resource
    CslxService cs;


    private static final Logger logger = LoggerFactory.getLogger(dzController.class);
    @RequestMapping("/getDz")
    public Result getDz(){
        List<dzModel> dz = ds.getDz();
        System.out.println("进入查询地址服务");
        return Result.success(dz);
    }

    @RequestMapping("/getDataBysbmc")
    public Result getDataBysbmc(dzModel params){
        System.out.println("进入查询地址服务:"+params.getPagesize());
        logger.info("进入查询地址服务:"+params.getPagesize()+"url"+url);
        PageInfo<dzModel> dz = ds.getDataBysbmc(params);
        System.out.println("进入查询地址服务:"+dz.toString());
        return Result.success(dz);
    }


    @RequestMapping("/getdzbie")
    public Result getdzbie(){


        List<Map<String, Object>> mapList = new ArrayList<>();
        mapList= ds.getDzbie();
       // System.out.println("进入查询地址服务:"+lx.getList());
        return Result.success(mapList);
    }


    @RequestMapping("/getdzbie2")
    public Result getdzbie2(){


        List<dzModel> list = ds.getDataBysbmc2();
        Map<String, Long> collect = list.stream()
                .filter(x -> ObjectUtil.isNotEmpty(x.getLx()))
                .collect(Collectors.groupingBy(dzModel::getLx, Collectors.counting()));
        // 最后返回给前端的数据结构
        List<Map<String, Object>> mapList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(collect)) {
            for (String key : collect.keySet()) {
                Map<String, Object> map = new HashMap<>();
                map.put("name", key);
                map.put("value", collect.get(key));
                mapList.add(map);
            }
        }
        return Result.success(mapList);
    }

    @RequestMapping("/getsblx")
    public Result getsblx(sblxModel params){
        System.out.println("进入查询场所类型:"+params.getPagesize());
        params.setPagenum(1);
        params.setPagesize(1000);
        PageInfo<sblxModel> lx = ds.getsblx(params);
        System.out.println("进入查询地址服务:"+lx.getList());
        return Result.success(lx);
    }

    @RequestMapping("/saveCslx")
    public Result saveCslx(@RequestBody sblxModel params){
        logger.info("进入新增场所类型:"+params.getId());
        if(params.getId()!=null){
            cs.updateCslx(params);
            return Result.success();
        }else{
            cs.saveCslx(params);

            return Result.success();
        }
    }
    @RequestMapping("/deleteCslxBatchIds")
    public Result deleteCslxBatchIds(@RequestBody List<sblxModel>  params){

        List<Integer> ids = params.stream().map(sblxModel -> sblxModel.getId())
                .collect(Collectors.toList());
        cs.deleteCslxBatchIds(ids);
        return Result.success();
    }

    @DeleteMapping("/Cslx/{id}")
    public Result deleteCslx(@PathVariable Integer id){
        logger.info("进入删除地址:"+id);
        cs.deleteCslx(id);
        return Result.success();
    }

    @RequestMapping("/savedz")
    public Result savedz(@RequestBody dzModel params){
        logger.info("进入新增场所:"+params.getId());
        if(params.getId()!=null){
            ds.updatedz(params);
            return Result.success();
        }else{
            ds.savedz(params);

            return Result.success();
        }
    }
        @DeleteMapping("/{id}")
        public Result deletedz(@PathVariable Integer id){
            logger.info("进入删除地址:"+id);
            ds.deletedz(id);
            return Result.success();
        }

    @PostMapping("/cslx/upload")
    public Result upload(MultipartFile file) throws IOException {
        List<sblxModel> infoList = ExcelUtil.getReader(file.getInputStream()).readAll(sblxModel.class);
        if (!CollectionUtil.isEmpty(infoList)) {
            for (sblxModel type : infoList) {
                try {
                    cs.saveCslx(type);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return Result.success();
    }

    @GetMapping("/cslx/export")
    public Result export(HttpServletResponse response) throws IOException {
        // 思考：
        // 要一行一行的组装数据，塞到一个list里面
        // 每一行数据，其实就对应数据库表中的一行数据，也就是对应Java的一个实体类Type
        // 我们怎么知道它某一列就是对应某个表头呢？？ 需要映射数据，我们需要一个Map<key,value>，把这个map塞到list里

        // 干！
        // 1. 从数据库中查询出所有数据
        List<sblxModel> all = cs.selectList();

        if (CollectionUtil.isEmpty(all)) {
            throw new CustomException("未找到数据");
        }

        // 2. 定义一个 List，存储处理之后的数据，用于塞到 list 里
        List<Map<String, Object>> list = new ArrayList<>(all.size());

        // 3. 定义Map<key,value> 出来，遍历每一条数据，然后封装到 Map<key,value> 里，把这个 map 塞到 list 里
        for (sblxModel type : all) {
            Map<String, Object> row = new HashMap<>();
            row.put("类型id", type.getId());
            row.put("类型名称", type.getLxmc());


            list.add(row);
        }

        // 4. 创建一个 ExcelWriter，把 list 数据用这个writer写出来（生成出来）
        ExcelWriter wr = ExcelUtil.getWriter(true);
        wr.write(list, true);

        // 5. 把这个 excel 下载下来
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=type.xlsx");
        ServletOutputStream out = response.getOutputStream();
        wr.flush(out, true);
        wr.close();
        IoUtil.close(System.out);


        return Result.success();
    }


}


