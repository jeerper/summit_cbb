package com.summit.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Api(description = "路由管理")
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

	@ApiOperation(value = "测试页面")
	@RequestMapping(value = "/logPage",method = RequestMethod.GET)
	public String  add() {
		return "haha";
	}

}
