package com.summit.controller;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.FunctionBean;
import com.summit.common.entity.LogBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.UserInfo;
import com.summit.common.entity.UserPassWordInfo;
import com.summit.common.redis.user.UserInfoCache;
import com.summit.common.util.ResultBuilder;
import com.summit.service.log.LogUtilImpl;
import com.summit.service.user.UserService;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Api(description = "用户管理")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    LogUtilImpl logUtil;
    @Autowired
    private UserService us;
    @Autowired
    UserInfoCache userInfoCache;
    
    @Value("${password.encode.key}")
    private String key;

    @PostMapping("/add")
    @ApiOperation(value = "新增用户",  notes = "昵称(name)，用户名(userName),密码(password)都是必输项")
    public RestfulEntityBySummit<String> add(@RequestBody UserInfo userInfo) {
        try {
            ResponseCodeEnum c=us.add(userInfo,key);
            if(c!=null){
            	 return ResultBuilder.buildError(c);
            }
            userInfoCache.setUserInfo(userInfo.getUserName(),userInfo);
            //LogBean logBean = new LogBean("用户管理","共享用户组件","新增用户信息："+userInfo.toString(),"1");
        	//logUtil.insertLog(logBean);
            return ResultBuilder.buildSuccess();
        } catch (Exception e) {
            logger.error("新增用户失败", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    /**
     * 此处的删除只修改状态。
     * @param userNames
     * @return
     */
    @ApiOperation(value = "删除用户信息")
    @DeleteMapping("/del")
    public RestfulEntityBySummit<String> del(
    		@RequestParam(value = "userNames") String userNames) {
        try {
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
            //LogBean logBean = new LogBean("用户管理","共享用户组件","删除用户信息："+userNames,"3");
            //logUtil.insertLog(logBean);
            return ResultBuilder.buildSuccess();
        } catch (Exception e) {
            logger.error("删除用户信息", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    @ApiOperation(value = "修改用户",  notes = "昵称(name)，用户名(userName),密码(password)都是必输项")
    @PostMapping("/edit")
    public RestfulEntityBySummit<String> edit(@RequestBody UserInfo userInfo) {
        try {
            ResponseCodeEnum c=us.edit(userInfo,key);
            if(c!=null){
            	 return ResultBuilder.buildError(c);
            }
            userInfoCache.setUserInfo(userInfo.getUserName(),userInfo);
            //LogBean logBean = new LogBean("用户管理","共享用户组件","修改用户信息："+userInfo,"2");
           // logUtil.insertLog(logBean);
            return ResultBuilder.buildSuccess();
        } catch (Exception e) {
            logger.error("修改用户失败:", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

   /* @ApiOperation(value = "修改密码")
    @PutMapping("/editPassword")
    public RestfulEntityBySummit<String> editPassword(
    		@RequestParam(value = "oldPassword")  String oldPassword,
    		@RequestParam(value = "password")  String password, 
    		@RequestParam(value = "repeatPassword")  String repeatPassword,
    		@RequestParam(value = "userName")  String userName) {
        LogBean logBean = new LogBean();
        try {
        	if(!password.equals(repeatPassword)){
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
    }*/

    @ApiOperation(value = "修改密码",  notes = "旧密码和新密码必须是加密后的数据")
    @PutMapping("/editPassword")
    public RestfulEntityBySummit<String> editPassword(@RequestBody UserPassWordInfo userPassWordInfo) {
        try {
            if(!userPassWordInfo.getPassword().equals(userPassWordInfo.getRepeatPassword())){
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4013);
            }
            ResponseCodeEnum ub=us.editPassword(userPassWordInfo.getUserName(),userPassWordInfo.getOldPassword(), userPassWordInfo.getPassword(), userPassWordInfo.getRepeatPassword(),key);
            if(ub!=null){
            	return ResultBuilder.buildError(ub);
            }
            //LogBean logBean = new LogBean("用户管理","共享用户组件","修改密码："+userPassWordInfo,"2");
            //logUtil.insertLog(logBean);
            return ResultBuilder.buildSuccess();
        } catch (Exception e) {
            logger.error("修改密码失败:", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    @ApiOperation(value = "根据用户名查询用户信息(对内接口),该接口只供注册中心使用")
    @GetMapping("/queryUserInfoByUserNameService")
    public RestfulEntityBySummit<UserInfo> queryUserInfoByUserNameService(
    		@RequestParam(value = "userName")  String userName) {
        try {
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
            return ResultBuilder.buildSuccess(ub);
        } catch (Exception e) {
            logger.error("根据用户名查询用户信息失败-对内接口：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }
    
    @ApiOperation(value = "根据用户名查询用户信息--对外接口")
    @GetMapping("/queryUserInfoByUserName")
    public RestfulEntityBySummit<UserInfo> queryUserInfoByUserName(
    		@RequestParam(value = "userName")  String userName) {
        try {
        	UserInfo ub = us.queryByUserName(userName);
            if (ub == null) {
            	 return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
            }
            ub.setPassword(null);
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
            JSONObject objcet= us.queryAdcdByUserName(userName);
           
            if(objcet!=null){
            	String[] adcdsArray = JSON.parseObject(objcet.get("adcds").toString(), new TypeReference<String[]>() {});
            	ub.setAdcds(adcdsArray);
            	String adnms=objcet.getString("adnms");
            	ub.setAdnms(adnms);
            }
            
            JSONObject objcetdept=  us.queryDeptByUserName(userName);
            if(objcetdept!=null ){
            	String[] deptsArray = JSON.parseObject(objcetdept.get("deptIds").toString(), new TypeReference<String[]>() {});
            	ub.setDepts(deptsArray);
            	String deptnames=objcetdept.getString("deptnames");
            	ub.setDeptNames(deptnames);
            }
            
            return ResultBuilder.buildSuccess(ub);
        } catch (Exception e) {
            logger.error("根据用户名查询用户信息失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }
    
    
    @ApiOperation(value = "根据用户名查询所有菜单")
    @GetMapping("/queryFunctionInfoByUserName")
    public RestfulEntityBySummit<List<FunctionBean>> queryFunctionInfoByUserName(
    		@RequestParam(value = "userName")  String userName) {
        try {
        	UserInfo ub = us.queryByUserName(userName);
            if (ub == null) {
            	 return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
            }
            List<FunctionBean> funList=us.getFunInfoByUserName(userName);
            return ResultBuilder.buildSuccess(funList);
            
        }catch (Exception e) {
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
        try {
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
            logger.error("用户分页查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }
    

    @ApiOperation(value = "用户分页查询")
    @GetMapping("/queryByPage")
    public RestfulEntityBySummit<Page<UserInfo>> queryByPage(
    		@RequestParam(value = "page") int page,
            @RequestParam(value ="pageSize") int pageSize,
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "userName",required = false) String userName,
            @RequestParam(value = "isEnabled",required = false) String isEnabled,
            @RequestParam(value = "state",required = false) String state) {
        try {
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
            logger.error("用户分页查询失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }
    


    @ApiOperation(value = "重置密码")
    @PutMapping("/resetPassword")
    public RestfulEntityBySummit<String> resetPassword(
    		@RequestParam(value = "userName") String userName) {
        try {
        	us.resetPassword(userName);
            //LogBean logBean = new LogBean("用户管理","共享用户组件","重置密码："+userName,"2");
            //logUtil.insertLog(logBean);
            return ResultBuilder.buildSuccess();
        } catch (Exception e) {
            logger.error("重置密码失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

    @ApiOperation(value = "根据用户名查询角色")
    @GetMapping("/queryRoleByUserName")
    public RestfulEntityBySummit<List<String>> queryRoleByUserName(
    		@RequestParam(value = "userName") String userName) {
        try {
        	UserInfo ub = us.queryByUserName(userName);
            if (ub == null) {
            	return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
            }
        	List<String> list=us.queryRoleByUserName(userName);
            return ResultBuilder.buildSuccess(list);
        } catch (Exception e) {
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
    		@RequestParam(value = "role",required = false) String[] role) {
        try {
        	UserInfo ub = us.queryByUserName(userName);
            if (ub == null) {
            	return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
            }
            us.grantRole(userName,role);
            //LogBean logBean = new LogBean("用户管理","共享用户组件","授权权限："+userName+",角色信息:"+role,"4");
            //logUtil.insertLog(logBean);
            return ResultBuilder.buildSuccess();
        } catch (Exception e) {
            logger.error("授权权限失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
    }

}
