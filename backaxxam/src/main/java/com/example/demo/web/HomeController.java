package com.example.demo.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;




@Controller
public class HomeController {
	
	
	@GetMapping("")
	public String index () {
		return "index";
	}
	
	
	@GetMapping("/success")
	public String success () {
		return "success";
		
	}
	

	@GetMapping("/cancel")
	public String cancel () {
		return "cancel";
		
	}

}
