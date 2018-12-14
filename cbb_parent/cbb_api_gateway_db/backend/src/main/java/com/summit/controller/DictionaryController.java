package com.summit.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/dictionary")
public class DictionaryController {


	@RequestMapping(value = "/logPage",method = RequestMethod.GET)
	public String  add() {
		return "haha";
	}

}
