package com.summit.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.summit.cbb.utils.page.Page;
import com.summit.common.CommonConstants;
import com.summit.common.MsgBean;
import com.summit.common.entity.*;
import com.summit.common.util.ResultBuilder;
import com.summit.service.auth.AuthService;
import com.summit.service.log.LogUtilImpl;
import com.summit.util.DateUtil;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Api(description = "基本信息审核管理")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/authController")
@Slf4j
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;
    @Autowired
    LogUtilImpl logUtil;

    @ApiOperation(value = "基本信息审核分页查询")
    @RequestMapping(value = "/queryByPage", method = RequestMethod.GET)
    public RestfulEntityBySummit<Page<AuthBean>> queryByPage(@ApiParam(value = "当前页，大于等于1") @RequestParam(value = "page", required = true) Integer page,
                                                             @ApiParam(value = "每页条数，大于等于0") @RequestParam(value = "pageSize", required = true) Integer pageSize,
                                                             @ApiParam(value = "修改类型") @RequestParam(value = "applytype", required = false) String applytype,
                                                             @ApiParam(value = "申请人姓名") @RequestParam(value = "applyName", required = false) String applyName,
                                                             @ApiParam(value = "起始时间") @RequestParam(value = "startTime", required = false) String startTime,
                                                             @ApiParam(value = "结束时间") @RequestParam(value = "endTime", required = false) String endTime) {

        try {
            page = (page == 0) ? 1 : page;
            pageSize = (pageSize == 0) ? SysConstants.PAGE_SIZE : pageSize;
            JSONObject paramJson = new JSONObject();
            if (!SummitTools.stringIsNull(applytype)) {
                paramJson.put("applytype", applytype);
            }
            if (!SummitTools.stringIsNull(applyName)) {
                paramJson.put("applyName", applyName);
            }
            if (!SummitTools.stringIsNull(startTime)) {
                paramJson.put("startTime", startTime);
            }
            if (!SummitTools.stringIsNull(endTime)) {
                paramJson.put("endTime", endTime);
            }
            Page<AuthBean> pageList = authService.queryByPage(page, pageSize, paramJson);
            return ResultBuilder.buildSuccess(pageList);
        } catch (Exception e) {
            logger.error("部门修改信息审核分页查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }


    @ApiOperation(value = "基本信息审核详情展示")
    @GetMapping("/queryAuthfoByAuthId")
    public RestfulEntityBySummit<Map<String, Object>> queryAuthfoByAuthId(@RequestParam(value = "id") String id) {
        try {
            Map<String, Object> authInfo = authService.findById(id);
            if (null != authInfo) {
                return ResultBuilder.buildSuccess(authInfo);
            }
        } catch (Exception ex) {
            logger.error("基本信息审核详情展示查询失败！", ex);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
        return null;
    }


    @ApiOperation(value = "参数为id数组,isAudited:审核方式(1:批准,2:拒绝)", notes = "根据id审核用户信息")
    @PostMapping(value = "/authByIdBatch")
    public RestfulEntityBySummit<String> authByIdBatch(@ApiParam(value = "主键id", required = true) @RequestParam(value = "authIds") List<String> authIds,
                                                       @ApiParam(value = "审核方式(1:批准,2:拒绝)")@RequestParam(value = "isAudited",required = true) String isAudited) {
        LogBean logBean = new LogBean();
        logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        try {
            int result=authService.authByIdBatch(authIds,isAudited);
            if(result == CommonConstants.UPDATE_ERROR){
                log.error("审核处理失败");
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999,"审核处理失败",null);
            }
        } catch (Exception e) {
            logger.error("用户信息审核失败:", e);
            logBean.setActionFlag("0");
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999, e.getMessage(), null);
        }
        SummitTools.getLogBean(logBean, "审核管理", "审核用户:" +authIds, "6");
        logUtil.insertLog(logBean);
        if ("0".equals(logBean.getActionFlag())) {
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        } else {
            return ResultBuilder.buildSuccess();
        }
    }
}