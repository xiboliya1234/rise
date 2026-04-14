package com.rise.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.rise.mapper.UserMapper;
import com.rise.model.ExcelModel;
import com.rise.util.ExcelDataListener;
import com.rise.util.ExcelUtil;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@CrossOrigin
@Controller
public class OriginalController {

	
	  @Autowired
	  private UserMapper UserMapper;
	  
	  private static final Logger logger = LoggerFactory.getLogger(OriginalController.class);
	@RequestMapping("/hello")
	@ResponseBody
	public String hello(){
		
		
		
		return "你好 springboot";
	    }
	
	
	//@ResponseBody
	//@RequestMapping(value = {"/getByParmsMap"}, method = RequestMethod.GET)
	//String  getByParmsMap(@RequestParam("id")int id){
	//	System.out.println(UserMapper.getuser(id));
	//	return UserMapper.getuser(id).toString();
	//	}
	
	
	@RequestMapping("/tc")
	public String tc(Model model){
		model.addAttribute("name","tangcheng");
		model.addAttribute("age",20);
		model.addAttribute("sex",'男');
		return "tc";
		
	}
	
	@RequestMapping("/totext")
	public String totext(){
		
		return "text";
		
	}
	
	@RequestMapping("/tovideo")
	public String tovideo(){
		
		return "video";
		
	}
	@RequestMapping("/tochatgptvideo")
	public String tochatgptvideo(){
		
		return "chatgptvideo";
		
	}

	//根据百度地图api获得经纬度
		@RequestMapping("/baiduMapApi")
		public String getXYBybaiduMapApi(){
			
			String qwe [] =UserMapper.getmapcoun1t();
			
			int count = UserMapper.getmapcount();
			
			for(int i=0;i<qwe.length;i++) {
				
				String result1= HttpUtil.get("https://api.map.baidu.com/geocoding/v3/?address="+qwe[i]+"&output=json&ak=5UvLUwN7pVl6WQSpet0nkqGAbVsoFGMl&callback=showLocation");
				
				result1 = result1.replaceAll("showLocation&&showLocation\\(", "");
				result1 = result1.replaceAll("\\)", "");
				
				JSONObject jsonObject = JSONUtil.parseObj(result1);
				
				 jsonObject = JSONUtil.parseObj(jsonObject.getStr("result"));
				 jsonObject = JSONUtil.parseObj(jsonObject.getStr("location"));
				
				 logger.info("lng:"+jsonObject.getStr("lng"));
				 logger.info("lat:"+ jsonObject.getStr("lat"));
				 logger.info("i:"+ qwe[i]);
				 UserMapper.updatesf4( jsonObject.getStr("lng"),  jsonObject.getStr("lat"), qwe[i]);
			}
			
			
			
			return "升天";
			
		}
		
		
		//根据百度地图api获得管辖区域
			@RequestMapping("/getGxqyBybaiduMapApi")
			public String getGxqyBybaiduMapApi(){
				String qwe [] =UserMapper.getgsmj();
				
				
				//String qwe [] =UserMapper.getx1y1();
				
				
				String xz = null;
				for(int i=0;i<qwe.length;i++) {
					
					
					
					String qwe1   =UserMapper.getx1y2(qwe[i]);
					logger.info("sql:"+"SELECT concat(x1,',',y1) FROM sf4 where   sbmc ="+qwe[i]+" limit 1");
					logger.info("qwe:"+qwe[i]);
					logger.info("x:"+qwe1.split(",")[0]);		
					logger.info("y:"+qwe1.split(",")[1]);
					
					
					String result1= HttpUtil.get("https://api.map.baidu.com/reverse_geocoding/v3/?ak=5UvLUwN7pVl6WQSpet0nkqGAbVsoFGMl&output=json&coordtype=wgs84ll&location="
					+qwe1.split(",")[1]+","+qwe1.split(",")[0]);
					//LOGGER.info("result1:"+result1);
					
					
					JSONObject jsonObject = JSONUtil.parseObj(result1);
					 jsonObject = JSONUtil.parseObj(jsonObject.getStr("result"));
					//LOGGER.info("formatted_address:"+jsonObject.getStr("formatted_address"));
					xz = jsonObject.getStr("formatted_address");
					jsonObject = JSONUtil.parseObj(jsonObject.getStr("addressComponent"));
					logger.info("city:"+jsonObject.getStr("city"));
					logger.info("district:"+jsonObject.getStr("district"));
					logger.info("town:"+jsonObject.getStr("town"));
					logger.info("qwe[i]:"+qwe[i]);
					
					
					
					
					 //jsonObject = JSONUtil.parseObj(jsonObject.getStr("location"));
					
					
					 UserMapper.updatesf41(jsonObject.getStr("city"), jsonObject.getStr("district"), jsonObject.getStr("town"), xz, qwe[i]);
				}
				
				
				
				return "升天";
				
			}
			
			
			
		    public static void ReadExcel() {
		    	String fileName = "C:\\Users\\13798\\Desktop\\" + "测试.xlsx";
		    	 EasyExcel.read(fileName, ExcelModel.class, new ExcelDataListener()).sheet().doRead();
		    }
		    
		    @ResponseBody
		    @RequestMapping("/updateExcel")
		    public static String  updateExcel( @RequestParam("file") MultipartFile file) throws Exception {
		    	logger.info("进入上传excel接口");
		    	//String fileName = "C:\\Users\\13798\\Desktop\\" + "测试.xlsx";
		    	EasyExcel.read(file.getInputStream(), ExcelModel.class, new ExcelDataListener()).sheet().doRead();
		    	JSONObject resultjson = JSONUtil.createObj();
		    	resultjson.set("code", "200");
				resultjson.set("message", "上传成功");
		    	// EasyExcel.read(fileName, ExcelModel.class, new ExcelDataListener()).sheet().doRead();
		    	 return resultjson.toString();
		    }
		    
		    public static void main(String args[]) {
		    	 ReadExcel();
		    }
		    
		    
	
}
