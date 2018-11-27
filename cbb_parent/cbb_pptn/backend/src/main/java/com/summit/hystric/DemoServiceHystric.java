package com.summit.hystric;

import org.springframework.stereotype.Component;

import com.summit.service.DemoService;

@Component
public class DemoServiceHystric implements DemoService{

	@Override
	public String demo(String name) {
		// TODO Auto-generated method stub
		return "sorry" + name +" the request got error";
	}

}
