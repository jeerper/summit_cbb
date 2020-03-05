package com.summit.controller;

import com.alibaba.fastjson.JSONObject;
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.*;
import com.summit.common.redis.user.UserInfoCache;
import com.summit.common.util.ResultBuilder;
import com.summit.dao.entity.LoginLogInfo;
import com.summit.service.log.LogUtilImpl;
import com.summit.service.user.UserAuditService;
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
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@Api(description = "用户审核管理")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/userAuditController")
@Slf4j
public class UserAuditController {
    private static final Logger logger = LoggerFactory.getLogger(UserAuditController.class);
    @Autowired
    LogUtilImpl logUtil;
    @Autowired
    UserInfoCache userInfoCache;
    @Autowired
    PermissionUtil permissionUtil;
    @Autowired
    private UserAuditService userAuditService;




    @ApiOperation(value = "用户信息审核",notes = "主键(id),用户名(username),审核方式(isAudited)都是必输项")
    @RequestMapping(value = "/userAudit", method = RequestMethod.GET)
    public RestfulEntityBySummit<String> userAudit( @ApiParam(value = "主键id")@RequestParam(value = "id",required = true) String id,
                                                    @ApiParam(value = "用户名")@RequestParam(value = "username",required = true) String username,
                                                    @ApiParam(value = "审核方式(1:通过,2:不通过)")@RequestParam(value = "isAudited",required = true) String isAudited) {
        LogBean logBean = new LogBean();
        logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        try{
            if (!permissionUtil.checkLoginUserAccessPermissionToOtherUser(username)) {
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4012);
            }
            ResponseCodeEnum c = userAuditService.userAudit(id,username, isAudited);
            if (c != null) {
                return ResultBuilder.buildError(c);
            }
        }catch (Exception e){
            logger.error("用户信息审核失败:", e);
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.getMessage());
        }
        SummitTools.getLogBean(logBean, "审核管理", "审核用户:" +username, "6");
        logUtil.insertLog(logBean);
        if ("0".equals(logBean.getActionFlag())) {
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        } else {
            return ResultBuilder.buildSuccess();
        }
    }

    @ApiOperation(value = "用户修改信息审核分页查询")
    @RequestMapping(value = "/queryByPage", method = RequestMethod.GET)
    public RestfulEntityBySummit<Page<UserAuditBean>> queryByPage(@ApiParam(value = "当前页，大于等于1") @RequestParam(value = "page",required = true) Integer page,
                                                                  @ApiParam(value = "每页条数，大于等于0") @RequestParam(value = "pageSize",required = true) Integer pageSize,
                                                                  @ApiParam(value = "姓名") @RequestParam(value = "name", required = false) String name,
                                                                  @ApiParam(value = "用户名")@RequestParam(value = "userName", required = false) String userName,
                                                                  @ApiParam(value = "是否启用")@RequestParam(value = "isEnabled", required = false) String isEnabled,
                                                                  @ApiParam(value = "电话号码") @RequestParam(value = "phone", required = false) String phone){
        try{
            page = (page == 0) ? 1 : page;
            pageSize = (pageSize == 0) ? SysConstants.PAGE_SIZE : pageSize;
            JSONObject paramJson = new JSONObject();
            if (!SummitTools.stringIsNull(userName)) {
                paramJson.put("userName", userName);
            }
            if (!SummitTools.stringIsNull(name)) {
                paramJson.put("name", name);
            }
            if (!SummitTools.stringIsNull(isEnabled)) {
                paramJson.put("isEnabled", isEnabled);
            }
            if (!SummitTools.stringIsNull(phone)) {
                paramJson.put("phone", phone);
            }
            Page<UserAuditBean> pageList = userAuditService.queryByPage(page, pageSize, paramJson);
            return ResultBuilder.buildSuccess(pageList);
        }catch (Exception e){
            logger.error("用户修改信息审核分页查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    @ApiOperation(value = "删除用户审核数据，参数为id数组", notes = "根据id删除用户审核信息")
    @DeleteMapping(value = "/delUserAuditByIdBatch")
    public RestfulEntityBySummit<String> delUserAudit(@ApiParam(value = "主键id", required = true) @RequestParam(value = "userAuditIds") List<String> userAuditIds) {
        try {
            userAuditService.delUserAudit(userAuditIds);
        } catch (Exception e) {
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999, e.getMessage(), null);
        }
        return ResultBuilder.buildError(ResponseCodeEnum.CODE_0000, "删除用户审核数据成功", null);
    }

}
