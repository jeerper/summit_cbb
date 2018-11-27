package com.summit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.summit.service.DemoService;

@RestController
public class DemoController {

	@Autowired
	DemoService demoService;
	
	 @GetMapping(value = "/hi")
	public String sayHi(@RequestParam String name){
		return "i am pptn demo";
	}
}
