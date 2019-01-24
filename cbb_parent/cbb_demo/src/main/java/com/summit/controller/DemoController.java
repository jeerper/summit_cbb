package com.summit.controller;

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestFulEntityBySummit;
import com.summit.service.DemoService;
import com.summit.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.Enumeration;

@Api(tags="Demo模块接口测试")
@RestController
@RequestMapping("/demo")
public class DemoController {

	@Autowired
	DemoService demoService;

	@Autowired
	UserService userService;


	@ApiOperation(value = "第一个demo接口,无参数传递,返回成功代码")
	@GetMapping(value = "/oneDemo")
	public RestFulEntityBySummit oneDemo() {
		return new RestFulEntityBySummit();
	}

	@ApiOperation(value = "第二个demo接口,无参数传递，返回失败代码")
	@GetMapping(value = "/twoDemo")
	public RestFulEntityBySummit twoDemo() {
		return new RestFulEntityBySummit<>(ResponseCodeBySummit.CODE_9999,null);
	}

	@ApiOperation(value = "第个demo接口,有参数传递，返回失败代码")
	@GetMapping(value = "/haveParamDemo")
	public RestFulEntityBySummit haveParamDemo() {


		return new RestFulEntityBySummit<>(ResponseCodeBySummit.CODE_9999,null);
	}


	@GetMapping(value = "/liuyuanss")
	public String liuyuan() {
		return "liuyuan";
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
