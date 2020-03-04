package com.summit.controller;

import com.summit.common.entity.LogBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.UserInfo;
import com.summit.common.redis.user.UserInfoCache;
import com.summit.common.util.ResultBuilder;
import com.summit.service.log.LogUtilImpl;
import com.summit.service.user.UserAuditService;
import com.summit.util.DateUtil;
import com.summit.util.PermissionUtil;
import com.summit.util.SummitTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

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



}
