package com.rise.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.github.pagehelper.PageInfo;
import com.rise.common.Result;
import com.rise.model.sblxModel;
import com.rise.model.userModel;
import com.rise.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rise.mapper.UserMapper;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

@CrossOrigin
@Controller
public class loginController {

	@Autowired
	UserMapper um;
	@Autowired
	UserService us;
	private static final Logger LOGGER = LoggerFactory.getLogger(loginController.class);
	
	
	
	@RequestMapping("/")
	public String homepage() {

		return "login";

	}
	
	@RequestMapping("/tologin")
	public String tologin() {

		return "login";

	}
	
	@RequestMapping("/toupdate")
	public String toupdate() {

		return "UpdateExcel";

	}
	
	@RequestMapping("/toMap")
	public String toMap() {

		return "map";

	}
	
	
	@ResponseBody
	@RequestMapping(value = { "/loginByUsernameAndPassword" }, method = RequestMethod.POST)
	String loginByUsernameAndPassword(@RequestBody String user)   {
		LOGGER.info("进入登录验证接口");
		System.out.println("进入登录验证接口");
		//System.out.println(user);
		JSONObject jsonObject = JSONUtil.parseObj(user);
		JSONObject resultjson = JSONUtil.createObj();

		int result = um.getuser(jsonObject.getStr("username"), jsonObject.getStr("password"));
		System.out.println("进入登录验证接口");
		if (result == 1) {
			System.out.println("用户"+jsonObject.getStr("username")+"登陆成功");
			um.updateuserstatus(jsonObject.getStr("username"), jsonObject.getStr("password"));
			resultjson.set("code", "200");
			resultjson.set("message", "登陆成功");
			//response.sendRedirect("jsp/function.jsp");
		return resultjson.toString();
		} else {
			System.out.println("用户"+jsonObject.getStr("username")+"登陆失败");
			resultjson.set("code", "500");
			resultjson.set("message", "密码或用户名错误,登陆失败");
			return resultjson.toString();
		}

	}




}
