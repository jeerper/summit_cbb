package com.summit.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.LogBean;
import com.summit.common.entity.QueryLogBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.UserInfo;
import com.summit.common.util.ResultBuilder;
import com.summit.service.log.LogUtilImpl;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Api(description = "日志管理")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/log")
@Slf4j
public class LogController {
	private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @Autowired
    LogUtilImpl logUtil;

    @PostMapping("/saveLog")
    @ApiOperation(value = "新增日志",  notes = "")
    public RestfulEntityBySummit<String> add(@RequestBody LogBean logBean) {
        try {
        	logUtil.insertLog(logBean);
            return ResultBuilder.buildSuccess();
        } catch (Exception e) {
            logger.error("新增日志失败", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }
    
    @ApiOperation(value = "修改日志",  notes = "昵称(id)必输项")
    @PostMapping("/editLog")
    public RestfulEntityBySummit<String> edit(@RequestBody LogBean logBean) {
        try {
        	logUtil.updateLog(logBean);
            return ResultBuilder.buildSuccess();
        } catch (Exception e) {
            logger.error("修改日志失败:", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    /**
     * 此处的删除只修改状态。
     * @param userNames
     * @return
     */
    @ApiOperation(value = "根据条件删除日志信息,日期条件根据stime(访问时间)字段删除")
    @DeleteMapping("/delLog")
    public RestfulEntityBySummit<String> delLog(
    		@RequestParam(value = "startDate",required = true) String startDate,
    		@RequestParam(value = "endDate",required = true) String endDate) {
        try {
        	logUtil.delLog(startDate,endDate);
            return ResultBuilder.buildSuccess();
        } catch (Exception e) {
            logger.error("删除用户信息", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }
    
    
  
	
   

    @ApiOperation(value = "日志分页查询")
    @GetMapping("/queryLogByPage")
    public RestfulEntityBySummit<Page<QueryLogBean>> queryByPage(
    		@RequestParam(value = "page") int page,
            @RequestParam(value ="pageSize") int pageSize,
            @RequestParam(value = "systemName",required = false) String systemName,
            @RequestParam(value = "funName",required = false) String funName,
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "startDate",required = false) String startDate,
    		@RequestParam(value = "endDate",required = false) String endDate) {
        try {
        	page = (page == 0) ? 1 : page;
            pageSize = (pageSize == 0) ? SysConstants.PAGE_SIZE : pageSize;
            
            JSONObject paramJson = new JSONObject();
            if(!SummitTools.stringIsNull(name)){
                paramJson.put("name",name);
            }
            if(!SummitTools.stringIsNull(systemName)){
                paramJson.put("systemName",systemName);
            }
            if(!SummitTools.stringIsNull(funName)){
                paramJson.put("funName",funName);
            }
            if(!SummitTools.stringIsNull(startDate)){
                paramJson.put("startDate",startDate);
            }
            if(!SummitTools.stringIsNull(endDate)){
                paramJson.put("endDate",endDate);
            }
            Page<QueryLogBean> pageList=logUtil.queryByPage(page, pageSize, paramJson);
            return ResultBuilder.buildSuccess(pageList);
        } catch (Exception e) {
            logger.error("用户分页查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

   

}
