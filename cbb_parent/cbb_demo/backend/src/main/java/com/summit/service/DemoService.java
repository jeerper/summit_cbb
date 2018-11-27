package com.summit.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.summit.hystric.DemoServiceHystric;

@FeignClient(value = "service-pptn",fallback = DemoServiceHystric.class)
public interface DemoService {
	@RequestMapping(value = "/hi",method = RequestMethod.GET)	
    String demo(@RequestParam(value = "name") String name);
}
