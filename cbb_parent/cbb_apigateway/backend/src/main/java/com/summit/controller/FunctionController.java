package com.summit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.summit.domain.FunctionEnum;
import com.summit.service.function.FunctionService;
import com.summit.util.SummitTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;

/**
 * 
 * @author yt
 *
 */
@RestController
@Api("功能模块")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/function")
public class FunctionController {

	@Autowired
	FunctionService functionService;
	
	@ApiOperation(value="新增功能",notes="用于application/json格式")
	@RequestMapping(value="/add", method = RequestMethod.POST)
	public Object add(@ApiParam(value="功能信息:example{\"PID\":\"admin\",\"FDESC\":1,\"FURL\":\"github.com\",\"IMGULR\":\"github.com\",\"IS_ENABLED\":1,\"NAME\":\"test data\",\"NOTE\":\"TEST\",\"SUPER_FUN\":0}") @RequestBody JSONObject jsonObject){
		if(jsonObject != null && !jsonObject.containsKey(FunctionEnum.ID.toString())){
			jsonObject.put(FunctionEnum.ID.toString(), SummitTools.getKey());
		}
		return  functionService.add(jsonObject);
	}
	
	
	@ApiOperation(value="修改功能",notes="用于application/json格式")
	@RequestMapping(value="/edit", method = RequestMethod.PUT)
	public Object edit(@ApiParam(value="功能信息:example{\"NAME\":\"admin\",\"FDESC\":1,\"FURL\":\"github.com\",\"IMGULR\":\"github.com\",\"IS_ENABLED\":1,\"NAME\":\"test data\",\"NOTE\":\"TEST\",\"ID\":\"id\"}") @RequestBody JSONObject jsonObject){
		
		return  functionService.edit(jsonObject);
	}
	
	@ApiOperation(value="根据id查询")
	@RequestMapping(value="/queryById", method = RequestMethod.POST)
	public Object queryById(@ApiParam(value="id") @RequestParam(name ="id") String id){
		return functionService.queryById(id);
	}
	
	@ApiOperation(value="查询所有,返回树")
	@RequestMapping(value="/queryTree", method = RequestMethod.POST)
	public Object queryTree(){
		return  functionService.queryAll();
	}
	
	@ApiOperation(value="分页查询")
	@RequestMapping(value="/queryByPage", method = RequestMethod.POST)
	public Object queryByPage(@ApiParam(value="pid") @RequestParam(name ="pid") String pid
			,@ApiParam(value="当前页") @RequestParam(name ="currentPage") Integer currentPage
			,@ApiParam(value="每页大小") @RequestParam(name ="pageSize") Integer pageSize
			,@ApiParam(value="跳转页") @RequestParam(name ="willJumpPage") Integer willJumpPage
			){
		return functionService.queryByPage(currentPage, pageSize, willJumpPage, pid);
	}
	
	@ApiOperation(value="删除")
	@RequestMapping(value="/delete", method = RequestMethod.DELETE)
	public Object delete(@ApiParam(value="ids") @RequestParam(name ="ids") String ids){
		
		return functionService.del(ids);
	}
}
