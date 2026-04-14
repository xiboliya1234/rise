package com.rise.service;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rise.controller.dzController;
import com.rise.dao.UserDao;
import com.rise.exception.CustomException;
import com.rise.model.userModel;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    UserDao ud;
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public List<userModel> login(@Param("ew")userModel params){

        if(StrUtil.isEmpty(params.getUsername())||StrUtil.isEmpty(params.getPassword())){
            throw new CustomException("用户名或密码不能为空");
        }


        QueryWrapper<userModel> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", params.getUsername());//tom_age必须是数据库中的字段
        queryWrapper.eq("password",params.getPassword());

        if(ud.selectCount(queryWrapper)==0){
            throw new CustomException("用户名或密码错误");
        }
        return ud.selectList(queryWrapper);
    }

    public Long findusernameByusername(@Param("cm")userModel params){
        QueryWrapper<userModel> queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", params.getUsername());//tom_age必须是数据库中的字段
        return ud.selectCount(queryWrapper);
    }

    public userModel findUserByID(Integer params) {
        return ud.selectById(params);
    }

    public void saveUser(userModel params) {
        int list = Math.toIntExact(findusernameByusername(params));
        if(list >0){
            throw new CustomException("用户"+params.getUsername()+"已存在");

        }
        if(StrUtil.isEmpty(params.getPassword())||StrUtil.isEmpty(params.getUsername())){
            throw new CustomException("用户名或密码不能为空");
        }
        params.setStatus(1);
        if(params.getPassword()==null){//如果没有设置密码，则设置密码为用户名
            params.setPassword(params.getUsername());
        }
        ud.insert(params);
    }

    public void updateUser(@Param("et") userModel params) {

        ud.updateById(params);
    }
}
