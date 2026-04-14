package com.rise.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.rise.dao.FileDao;
import com.rise.exception.CustomException;
import com.rise.model.fileModel;
import com.rise.model.userModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class FileService {

    @Resource
    FileDao fd;

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    public static void  main(String[] args) {

      //  System.out.println(date);
    }


    public void saveFile(fileModel params) {
        Date date = DateUtil.date();
        params.setCreatedate(date.toString());


        fd.insert(params);
    }
}
