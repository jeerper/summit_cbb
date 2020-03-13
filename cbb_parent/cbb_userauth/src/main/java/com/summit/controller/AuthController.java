package com.summit.controller;

import com.alibaba.fastjson.JSONObject;
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.AuthBean;
import com.summit.common.entity.DeptAuditBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.util.ResultBuilder;
import com.summit.service.auth.AuthService;
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

@Api(description = "基本信息审核管理")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/authController")
@Slf4j
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;




    @ApiOperation(value = "基本信息审核分页查询")
    @RequestMapping(value = "/queryByPage", method = RequestMethod.GET)
    public RestfulEntityBySummit<Page<AuthBean>> queryByPage(@ApiParam(value = "当前页，大于等于1") @RequestParam(value = "page", required = true) Integer page,
                                                                  @ApiParam(value = "每页条数，大于等于0") @RequestParam(value = "pageSize", required = true) Integer pageSize,
                                                                  @ApiParam(value = "修改类型") @RequestParam(value = "applytype", required = false) String applytype,
                                                                  @ApiParam(value = "申请人姓名") @RequestParam(value = "applyName", required = false) String applyName,
                                                                  @ApiParam(value = "起始时间") @RequestParam(value = "startTime", required = false) String startTime,
                                                                  @ApiParam(value = "结束时间") @RequestParam(value = "endTime", required = false) String endTime) {

        try{
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
        }catch (Exception e){
            logger.error("部门修改信息审核分页查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }


    }
}