package com.rise.controller;

import com.rise.mapper.UserMapper;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class loginController {

    @Autowired
    UserMapper um;

    private static final Logger LOGGER = LoggerFactory.getLogger(loginController.class);

    @RequestMapping(value = { "/loginByUsernameAndPassword" }, method = RequestMethod.POST)
    String loginByUsernameAndPassword(@RequestBody String user) {
        LOGGER.info("进入登录验证接口");

        JSONObject jsonObject = JSONUtil.parseObj(user);
        JSONObject resultjson = JSONUtil.createObj();

        int result = um.getuser(jsonObject.getStr("username"), jsonObject.getStr("password"));

        if (result == 1) {
            um.updateuserstatus(jsonObject.getStr("username"), jsonObject.getStr("password"));
            resultjson.set("code", "200");
            resultjson.set("message", "登录成功");
            return resultjson.toString();
        } else {
            resultjson.set("code", "500");
            resultjson.set("message", "密码或用户名错误,登录失败");
            return resultjson.toString();
        }
    }
}
