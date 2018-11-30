package com.summit.controller;

import java.io.BufferedReader;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

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
	public String sayHi(@RequestParam String name) {
		return demoService.demo(name);
	}

	@RequestMapping("/testDemo1")
	public String demo1(HttpServletRequest request) {
		System.out.println("进入此方法");
		//测试是否能取到请求头中的token
        Enumeration<String> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String nextElement = headerNames.nextElement();
            System.out.println(nextElement+":"+request.getHeader(nextElement));
        }
		 return userService.getAllUser1().toString();
	}

	@RequestMapping("/testDemo2")
	public String demo2() {
		return userService.getAllUser2().toString();
	}

	public String getBodyString(BufferedReader br) {
		String inputLine;
		String str = "";
		try {
			while ((inputLine = br.readLine()) != null) {
				str += inputLine;
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str;
	}
}
