package com.summit.controller;

import com.summit.common.entity.FunctionBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.UserInfo;
import com.summit.common.redis.user.UserInfoCache;
import com.summit.common.util.ResultBuilder;
import com.summit.domain.log.LogBean;
import com.summit.service.log.ILogUtil;
import com.summit.service.user.UserService;
import com.summit.util.Page;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(description = "用户管理")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    ILogUtil logUtil;
    @Autowired
    private UserService us;
    @Autowired
    UserInfoCache userInfoCache;

    @PostMapping("/add")
    @ApiOperation(value = "新增用户",  notes = "昵称(name)，用户名(userName),密码(password)都是必输项")
    public RestfulEntityBySummit<String> add(@RequestBody UserInfo userInfo) {
       LogBean logBean = new LogBean();
        try {
        	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    		logBean = logUtil.insertLog(request, "1", "用户新增", userInfo.getUserName());
            ResponseCodeEnum c=us.add(userInfo);
            if(c!=null){
            	 return ResultBuilder.buildError(c);
            }
            return ResultBuilder.buildSuccess();
        } catch (Exception e) {
           //e.printStackTrace();
            logger.error("新增用户失败", e);
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.getMessage());
            logUtil.updateLog(logBean, "1");
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    /**
     * 此处的删除只修改状态。
     * @param userNames
     * @param request
     * @param userName
     * @return
     */
    @ApiOperation(value = "删除用户信息")
    @DeleteMapping("/del")
    public RestfulEntityBySummit<String> del(
    		@RequestParam(value = "userNames") String userNames) {
        LogBean logBean = new LogBean();
        try {
        	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
    		 logBean = logUtil.insertLog(request, "1", "删除用户", "");
            if(userNames.contains(",")){
            	for(String username:userNames.split(",")){
            		//系统管路员用户不能删除
            		if (SummitTools.stringEquals(SysConstants.SUPER_USERNAME, username)) {
            			continue;
                    }
            		userInfoCache.deleteUserInfo(username);		
            	}
            }
            us.del(userNames);
            return ResultBuilder.buildSuccess();
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("删除用户信息", e);
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    @ApiOperation(value = "修改用户",  notes = "昵称(name)，用户名(userName),密码(password)都是必输项")
    @PostMapping("/edit")
    public RestfulEntityBySummit<String> edit(@RequestBody UserInfo userInfo) {
        LogBean logBean = new LogBean();
        try {
        	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            logBean = logUtil.insertLog(request, "1", "修改用户", "");
            	userInfoCache.setUserInfo(userInfo.getUserName(),userInfo);
            	us.edit(userInfo);
            	return ResultBuilder.buildSuccess();
        } catch (Exception e) {
            //e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            logger.error("修改用户失败:", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    @ApiOperation(value = "修改密码")
    @PutMapping("/editPassword")
    public RestfulEntityBySummit<String> editPassword(
    		@RequestParam(value = "oldPassword")  String oldPassword,
    		@RequestParam(value = "password")  String password, 
    		@RequestParam(value = "repeatPassword")  String repeatPassword,
    		@RequestParam(value = "userName")  String userName) {
        LogBean logBean = new LogBean();
        try {
        	if(!oldPassword.equals(repeatPassword)){
        		return ResultBuilder.buildError(ResponseCodeEnum.CODE_4013); 
        	}
        	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            logBean = logUtil.insertLog(request, "1", "修改密码", userName);
            us.editPassword(userName,oldPassword, password, repeatPassword);
            return ResultBuilder.buildSuccess();
        } catch (Exception e) {
            //e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            logger.error("修改密码失败:", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    @ApiOperation(value = "根据用户名查询用户信息")
    @GetMapping("/queryUserInfoByUserName")
    public RestfulEntityBySummit<UserInfo> queryUserInfoByUserName(
    		@RequestParam(value = "userName")  String userName) {
        LogBean logBean = new LogBean();
        try {
        	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            logBean = logUtil.insertLog(request, "1", "用户管理根据用户名查询用户", userName);
            UserInfo ub = us.queryByUserName(userName);
            if (ub == null) {
            	 return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
            }
            List<String> roleList = us.queryRoleByUserName(userName);
            List<String> funList = us.getFunByUserName(userName);
            
            if(roleList!=null && roleList.size()>0){
            	String[] roleArray = new String[roleList.size()];
            	roleList.toArray(roleArray);
                ub.setRoles(roleArray);	
            }
            if(funList!=null && funList.size()>0){
            	String[] funArray = new String[funList.size()];
            	funList.toArray(funArray);
            	ub.setPermissions(funArray);
            }
            
            String[] adcdsArray = us.queryAdcdByUserName(userName);
            if(adcdsArray!=null && adcdsArray.length>0){
            	ub.setAdcds(adcdsArray);
            }
            
            String[] deptsArray = us.queryDeptByUserName(userName);
            if(deptsArray!=null && deptsArray.length>0){
            	ub.setDepts(deptsArray);
            }
            return ResultBuilder.buildSuccess(ub);
        } catch (Exception e) {
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            logger.error("根据用户名查询用户信息失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }
    
    @ApiOperation(value = "根据用户名查询所有菜单")
    @GetMapping("/queryFunctionInfoByUserName")
    public RestfulEntityBySummit<List<FunctionBean>> queryFunctionInfoByUserName(
    		@RequestParam(value = "userName")  String userName) {
        LogBean logBean = new LogBean();
        try {
        	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            logBean = logUtil.insertLog(request, "1", "用户管理根据用户名查询菜单信息", userName);
            UserInfo ub = us.queryByUserName(userName);
            if (ub == null) {
            	 return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
            }
            List<FunctionBean> funList=us.getFunInfoByUserName(userName);
            
            return ResultBuilder.buildSuccess(funList);
            
        }catch (Exception e) {
            //e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            logger.error("根据用户名查询所有菜单失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    @ApiOperation(value = "根据条件查询用户信息不分页")
    @GetMapping("/queryAllUser")
    public RestfulEntityBySummit<List<UserInfo>> queryAllUser(
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "userName",required = false) String userName,
            @RequestParam(value = "isEnabled",required = false) String isEnabled,
            @RequestParam(value = "state",required = false) String state) {
        LogBean logBean = new LogBean();
        try {
        	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            logBean = logUtil.insertLog(request, "1", "用户管理分页查询", "");
            JSONObject paramJson = new JSONObject();
            if(!SummitTools.stringIsNull(name)){
                paramJson.put("name",name);
            }
            if(!SummitTools.stringIsNull(userName)){
                paramJson.put("userName",userName);
            }
            if(!SummitTools.stringIsNull(isEnabled)){
                paramJson.put("isEnabled",isEnabled);
            }
            if(!SummitTools.stringIsNull(state)){
                paramJson.put("state",state);
            }
            List<UserInfo> pageList=us.queryUserInfoList( paramJson);
            return ResultBuilder.buildSuccess(pageList);
        } catch (Exception e) {
            //e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            logger.error("用户分页查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }
    

    @ApiOperation(value = "分页查询")
    @GetMapping("/queryByPage")
    public RestfulEntityBySummit<Page<UserInfo>> queryByPage(
    		@RequestParam(value = "page") int page,
            @RequestParam(value ="pageSize") int pageSize,
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "userName",required = false) String userName,
            @RequestParam(value = "isEnabled",required = false) String isEnabled,
            @RequestParam(value = "state",required = false) String state) {
        LogBean logBean = new LogBean();
        try {
        	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            logBean = logUtil.insertLog(request, "1", "用户管理分页查询", "");
            page = (page == 0) ? 1 : page;
            pageSize = (pageSize == 0) ? SysConstants.PAGE_SIZE : pageSize;
            
            JSONObject paramJson = new JSONObject();
            if(!SummitTools.stringIsNull(name)){
                paramJson.put("name",name);
            }
            if(!SummitTools.stringIsNull(userName)){
                paramJson.put("userName",userName);
            }
            if(!SummitTools.stringIsNull(isEnabled)){
                paramJson.put("isEnabled",isEnabled);
            }
            if(!SummitTools.stringIsNull(state)){
                paramJson.put("state",state);
            }
            Page<UserInfo> pageList=us.queryByPage(page, pageSize, paramJson);
            return ResultBuilder.buildSuccess(pageList);
        } catch (Exception e) {
            //e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            logger.error("用户分页查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    @ApiOperation(value = "重置密码")
    @PutMapping("/resetPassword")
    public RestfulEntityBySummit<String> resetPassword(
    		@RequestParam(value = "userName") String userName) {
        LogBean logBean = new LogBean();
        try {
        	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            logBean = logUtil.insertLog(request, "1", "用户管理重置密码", userName);
            us.resetPassword(userName);
            return ResultBuilder.buildSuccess();
        } catch (Exception e) {
            //e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            logger.error("重置密码失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    @ApiOperation(value = "根据用户名查询角色")
    @GetMapping("/queryRoleByUserName")
    public RestfulEntityBySummit<List<String>> queryRoleByUserName(
    		@RequestParam(value = "userName") String userName) {
       // LogBean logBean = new LogBean();
        try {
        	//HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        	UserInfo ub = us.queryByUserName(userName);
            if (ub == null) {
            	return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
            }
        	List<String> list=us.queryRoleByUserName(userName);
           // logBean = logUtil.insertLog(request, "1", "用户管理查询用户角色", userName);
            return ResultBuilder.buildSuccess(list);
        } catch (Exception e) {
            //e.printStackTrace();
           // logBean.setActionFlag("0");
            //logBean.setErroInfo(e.toString());
            //logUtil.updateLog(logBean, "1"); 
            logger.error("根据用户名查询角色失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }
    
    @ApiOperation(value = "根据用户名查询角色--基于antd：Transfer穿梭框")
    @GetMapping("/queryRoleListAntdByUserName")
    public RestfulEntityBySummit<List<String>> queryRoleListAntdByUserName(
    		@RequestParam(value = "userName") String userName) {
	        try {
	        	UserInfo ub = us.queryByUserName(userName);
	            if (ub == null) {
	            	return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
	            }
	        	List<String> list=us.queryRoleListByUserName(userName);
	            return ResultBuilder.buildSuccess(list);
	        } catch (Exception e) {
	            logger.error("根据用户名查询角色失败：", e);
	            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
	        }
    }
    
    
    

    @ApiOperation(value = "授权权限")
    @PutMapping("/grantRole")
    public RestfulEntityBySummit<String>  grantRole(
    		@RequestParam(value = "userName") String userName,
    		@RequestParam(value = "role") String role) {
        LogBean logBean = new LogBean();
        try {
        	HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
            logBean = logUtil.insertLog(request, "1", "用户管理授权", userName);
            UserInfo ub = us.queryByUserName(userName);
            if (ub == null) {
            	return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
            }
            us.grantRole(userName,role);
            return ResultBuilder.buildSuccess();
            //return  new RestfulEntityBySummit<String>(,null);
        } catch (Exception e) {
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            logger.error("授权权限失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

}
