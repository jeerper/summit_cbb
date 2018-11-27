package com.summit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.summit.service.DemoService;
import com.summit.service.UserService;

@RestController
public class DemoController {

	@Autowired
	DemoService demoService;
	
	@Autowired
	UserService userService;
	 @GetMapping(value = "/hi")
	public String sayHi(@RequestParam String name){
		return demoService.demo(name);
	}
	 
	 @RequestMapping("/testDemo1")
	 public String demo1(){
		return userService.getAllUser1().toString();
	 }
	 
	 @RequestMapping("/testDemo2")
	 public String demo2(){
		return userService.getAllUser2().toString();
	 }
}
