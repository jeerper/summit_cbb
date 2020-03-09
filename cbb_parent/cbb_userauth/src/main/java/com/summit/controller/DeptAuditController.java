package com.summit.controller;

import com.alibaba.fastjson.JSONObject;
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.*;
import com.summit.common.redis.user.UserInfoCache;
import com.summit.common.util.ResultBuilder;
import com.summit.service.dept.DeptAuditService;
import com.summit.service.log.LogUtilImpl;
import com.summit.util.DateUtil;
import com.summit.util.PermissionUtil;
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

@Api(description = "部门审核管理")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/deptAuditController")
@Slf4j
public class DeptAuditController {
    private static final Logger logger = LoggerFactory.getLogger(DeptAuditController.class);
    @Autowired
    LogUtilImpl logUtil;
    @Autowired
    UserInfoCache userInfoCache;
    @Autowired
    PermissionUtil permissionUtil;
    @Autowired
    private DeptAuditService deptAuditService;


    @ApiOperation(value = "部门信息审核", notes = "主键(id),部门id(deptIdAuth),审核方式(isAudited)都是必输项")
    @RequestMapping(value = "/deptAudit", method = RequestMethod.GET)
    public RestfulEntityBySummit<String> deptAudit(@ApiParam(value = "主键id") @RequestParam(value = "id", required = true) String id,
                                                   @ApiParam(value = "部门id") @RequestParam(value = "deptIdAuth", required = true) String deptIdAuth,
                                                   @ApiParam(value = "审核方式(1:通过,2:不通过)") @RequestParam(value = "isAudited", required = true) String isAudited) {
        LogBean logBean = new LogBean();
        logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        try{
            ResponseCodeEnum c = deptAuditService.deptAudit(id,deptIdAuth, isAudited);
            if (c != null) {
                return ResultBuilder.buildError(c);
            }
        }catch (Exception e){
            logger.error("部门信息审核失败:", e);
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.getMessage());
        }
        SummitTools.getLogBean(logBean, "审核管理", "审核部门:" +deptIdAuth, "6");
        logUtil.insertLog(logBean);
        if ("0".equals(logBean.getActionFlag())) {
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        } else {
            return ResultBuilder.buildSuccess();
        }
    }


    @ApiOperation(value = "部门修改信息审核分页查询")
    @RequestMapping(value = "/queryByPage", method = RequestMethod.GET)
    public RestfulEntityBySummit<Page<DeptAuditBean>> queryByPage(@ApiParam(value = "当前页，大于等于1") @RequestParam(value = "page",required = true) Integer page,
                                                                  @ApiParam(value = "每页条数，大于等于0") @RequestParam(value = "pageSize",required = true) Integer pageSize,
                                                                  @ApiParam(value = "部门姓名") @RequestParam(value = "deptNameAuth", required = false) String deptNameAuth,
                                                                  @ApiParam(value = "审核人")@RequestParam(value = "authPerson", required = false) String authPerson){
        try{
            page = (page == 0) ? 1 : page;
            pageSize = (pageSize == 0) ? SysConstants.PAGE_SIZE : pageSize;
            JSONObject paramJson = new JSONObject();
            if (!SummitTools.stringIsNull(deptNameAuth)) {
                paramJson.put("deptNameAuth", deptNameAuth);
            }
            if (!SummitTools.stringIsNull(authPerson)) {
                paramJson.put("authPerson", authPerson);
            }
            Page<DeptAuditBean> pageList = deptAuditService.queryByPage(page, pageSize, paramJson);
            return ResultBuilder.buildSuccess(pageList);

        }catch (Exception e){
            logger.error("部门修改信息审核分页查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }

    }



}