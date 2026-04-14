package com.rise.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
@CrossOrigin
@Controller
public class JumpController {
	
	
	@RequestMapping("/jumptofunction")
	public String jumptofunction() {

		return "jsp/function";

	}
	
	@RequestMapping("/jumptologin")
	public String jumptologin() {
       
		
		
		
		return "login";

	}
	
	
	@RequestMapping("/jumptosignup")
	public String jumptosignup() {
       
		
		
		
		return "jsp/login/signup";

	}
	
	@RequestMapping("/jumptoMap")
	public String toMap() {

		return "map";

	}
	
	@RequestMapping("/jumptoupdate")
	public String jumptoupdate() {

		return "UpdateExcel";

	}
}
