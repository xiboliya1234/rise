package com.rise.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.rise.mapper.UserMapper;
import com.rise.model.ExcelModel;
import com.rise.util.ExcelDataListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
public class OriginalController {

    @Autowired
    private UserMapper userMapper;

    private static final Logger logger = LoggerFactory.getLogger(OriginalController.class);

    @RequestMapping("/hello")
    public String hello() {
        return "浣犲ソ springboot";
    }

    // Legacy utility endpoint: refresh map coordinates from Baidu API
    @RequestMapping("/baiduMapApi")
    public String getXYBybaiduMapApi() {

        String[] qwe = userMapper.getmapcoun1t();

        for (String s : qwe) {
            String result1 = HttpUtil.get("https://api.map.baidu.com/geocoding/v3/?address=" + s
                    + "&output=json&ak=5UvLUwN7pVl6WQSpet0nkqGAbVsoFGMl&callback=showLocation");

            result1 = result1.replaceAll("showLocation&&showLocation\\(", "");
            result1 = result1.replaceAll("\\)", "");

            JSONObject jsonObject = JSONUtil.parseObj(result1);

            jsonObject = JSONUtil.parseObj(jsonObject.getStr("result"));
            jsonObject = JSONUtil.parseObj(jsonObject.getStr("location"));

            logger.info("lng:{}", jsonObject.getStr("lng"));
            logger.info("lat:{}", jsonObject.getStr("lat"));
            logger.info("name:{}", s);
            userMapper.updatesf4(jsonObject.getStr("lng"), jsonObject.getStr("lat"), s);
        }

        return "ok";
    }

    // 鏍规嵁鐧惧害鍦板浘 api 鑾峰彇绠¤緰鍖哄煙
    @RequestMapping("/getGxqyBybaiduMapApi")
    public String getGxqyBybaiduMapApi() {
        String[] qwe = userMapper.getgsmj();

        String xz = null;
        for (String s : qwe) {
            String qwe1 = userMapper.getx1y2(s);
            logger.info("sql:SELECT concat(x1,',',y1) FROM sf4 where sbmc={} limit 1", s);
            logger.info("qwe:{}", s);
            logger.info("x:{}", qwe1.split(",")[0]);
            logger.info("y:{}", qwe1.split(",")[1]);

            String result1 = HttpUtil.get(
                    "https://api.map.baidu.com/reverse_geocoding/v3/?ak=5UvLUwN7pVl6WQSpet0nkqGAbVsoFGMl&output=json&coordtype=wgs84ll&location="
                            + qwe1.split(",")[1] + "," + qwe1.split(",")[0]);

            JSONObject jsonObject = JSONUtil.parseObj(result1);
            jsonObject = JSONUtil.parseObj(jsonObject.getStr("result"));
            xz = jsonObject.getStr("formatted_address");
            jsonObject = JSONUtil.parseObj(jsonObject.getStr("addressComponent"));
            logger.info("city:{}", jsonObject.getStr("city"));
            logger.info("district:{}", jsonObject.getStr("district"));
            logger.info("town:{}", jsonObject.getStr("town"));
            logger.info("qwe[i]:{}", s);

            userMapper.updatesf41(jsonObject.getStr("city"), jsonObject.getStr("district"), jsonObject.getStr("town"), xz, s);
        }

        return "ok";
    }

    public static void ReadExcel() {
        String fileName = "C:\\Users\\13798\\Desktop\\娴嬭瘯.xlsx";
        EasyExcel.read(fileName, ExcelModel.class, new ExcelDataListener()).sheet().doRead();
    }

    @ResponseBody
    @RequestMapping("/updateExcel")
    public static String updateExcel(@RequestParam("file") MultipartFile file) throws Exception {
        logger.info("杩涘叆涓婁紶 excel 鎺ュ彛");
        EasyExcel.read(file.getInputStream(), ExcelModel.class, new ExcelDataListener()).sheet().doRead();
        JSONObject resultjson = JSONUtil.createObj();
        resultjson.set("code", "200");
        resultjson.set("message", "涓婁紶鎴愬姛");
        return resultjson.toString();
    }

    public static void main(String[] args) {
        ReadExcel();
    }
}
