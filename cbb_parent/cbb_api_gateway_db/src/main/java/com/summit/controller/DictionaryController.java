package com.summit.controller;

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestFulEntityBySummit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Api(description = "路由管理")
@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

	@ApiOperation(value = "测试页面",notes = "asdsad")
	@RequestMapping(value = "/logPage",method = RequestMethod.GET)
	public RestFulEntityBySummit add() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		return new RestFulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,"haha");
	}

}
