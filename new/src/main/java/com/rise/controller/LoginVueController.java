package com.rise.controller;


import com.rise.common.Result;
import com.rise.exception.CustomException;
import com.rise.model.dzModel;
import com.rise.model.userModel;
import com.rise.service.UserService;
import com.rise.util.JwtTokenUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/vue/login")
public class LoginVueController {

    private static final Logger logger = LoggerFactory.getLogger(LoginVueController.class);
    @Resource
    UserService us;

    @RequestMapping("/getlogin")
    public Result login(@RequestBody userModel params){
        System.out.println("进入登录:"+params.getUsername());



        userModel user = us.login(params).get(0);

        String token = JwtTokenUtils.genToken(user.getId().toString(), user.getPassword());
        logger.info("token:"+token);
        //user.setToken(token);
        user.setToken(token);
        return Result.success(user);
    }

    @RequestMapping("/gettoken")
    public Result gettoken(@RequestBody userModel params){
        System.out.println("进入登录:"+params.getUsername());

        userModel user = us.login(params).get(0);

        String token = JwtTokenUtils.genToken(user.getId().toString(), user.getPassword());
        logger.info("token:"+token);
        //user.setToken(token);
        user.setToken(token);
        return Result.success(token);
    }


    @RequestMapping("/saveUser")
    public Result savedz(@RequestBody userModel params){
        logger.info("进入新增用户:"+params.getId());
        if(params.getId()!=null){
             us.updateUser(params);
            return Result.success();
        }else{
            us.saveUser(params);

            return Result.success();
        }
    }
}
