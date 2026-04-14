package com.rise.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.rise.common.Result;
import com.rise.model.fileModel;
import com.rise.service.FileService;
import com.rise.util.ImageToBase64Util;
import com.rise.util.ansj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.net.DatagramSocket;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  文件上传接口
 */
@RestController
@RequestMapping("/vue/files")
public class FileController {
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    @Resource
    FileService fs;
    
    
    // 文件上传存储路径
    @Value("${riseout.fileuploadpath}")
    String filePath ;


    public static void main(String[] args) {
       // System.out.println(filePath);
    }

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile file, HttpServletRequest request) {
        synchronized (FileController.class) {
            String flag = System.currentTimeMillis() + "";
            logger.info("文件上传flag:{}", flag);
            String fileName = file.getOriginalFilename();
            logger.info("文件名:{}", fileName);
            fileModel fm = new fileModel();
            String ipAddress = request.getRemoteAddr();
                   int port = request.getLocalPort();

            // 文件存储形式：时间戳-文件名
            try {
                if (!FileUtil.isDirectory(filePath)) {//判断是否有这个文件夹
                    FileUtil.mkdir(filePath);//没有则创建一个
                }
                // 文件存储形式：时间戳-文件名
                //logger.info("访问该接口的IP地址:{}", );

                FileUtil.writeBytes(file.getBytes(), filePath + flag + "-" + fileName);
                System.out.println(fileName + "--上传成功");

                fm.setFilename(fileName);
                fm.setFilepath(filePath + flag + "-" + fileName);
                fm.setFilecode(flag);
                fm.setDownpath("http://"+ipAddress+":"+port+"/vue/files/"+flag + "-" + fileName);

                fs.saveFile(fm);
                Thread.sleep(1L);
            } catch (Exception e) {
                System.err.println(fileName + "--文件上传失败");
            }
            return Result.success(fm);
        }
    }


    /**
     * 文件上传
     */
    @PostMapping("/uploadIMG")
    public String uploadIMG(MultipartFile file, HttpServletRequest request) {
        synchronized (FileController.class) {

            String fileName = file.getOriginalFilename();
            logger.info("文件名:{}", fileName);
            fileModel fm = new fileModel();
            Map<String, Object> arguments = new HashMap<>();

            File file2 = ImageToBase64Util.transferToFile(file);
            String ocrResult = ImageToBase64Util.imgToText(file2);
            //String base64 =ImageToBase64Util.updateUserAvatar(file);
           // logger.info("base64转码后:"+base64);
            // 文件存储形式：时间戳-文件名
            JSONObject resultjson = JSONUtil.createObj();
            resultjson.set("code", "200");
            resultjson.set("ocrResult", ocrResult);

            logger.info("ocr结果:"+ocrResult);
            String nlpansj = ansj.nlpfc(ocrResult);
            resultjson.set("nlpansj", nlpansj);
            return resultjson.toString();
        }
    }


    /**
     * 获取文件
     */
    @GetMapping("/{flag}")
    public void avatarPath(@PathVariable String flag, HttpServletResponse response) {
        // 判断文件路径是否存在，不存在则创建
        if (!FileUtil.isDirectory(filePath)) {
            FileUtil.mkdir(filePath);
        }
        OutputStream os;
        // 获取文件夹下的文件名列表
        List<String> fileNames = FileUtil.listFileNames(filePath);
        // 从文件名列表中获取包含flag的文件名
        String avatar = fileNames.stream().filter(name -> name.contains(flag)).findAny().orElse("");
        try {
            // 如果文件名不为空，则设置响应头，并返回文件
            if (StrUtil.isNotEmpty(avatar)) {
                response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(avatar, "UTF-8"));
                response.setContentType("application/octet-stream");
                byte[] bytes = FileUtil.readBytes(filePath + avatar);
                os = response.getOutputStream();
                os.write(bytes);
                os.flush();
                os.close();
            }
        } catch (Exception e) {
            System.out.println("文件下载失败");
        }
    }

}