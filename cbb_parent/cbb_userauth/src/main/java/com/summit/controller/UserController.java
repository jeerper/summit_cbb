package com.summit.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.summit.MainAction;
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.FunctionBean;
import com.summit.common.entity.LogBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.UserInfo;
import com.summit.common.entity.UserPassWordInfo;
import com.summit.common.redis.user.UserInfoCache;
import com.summit.common.util.ResultBuilder;
import com.summit.common.web.filter.UserContextHolder;
import com.summit.service.log.LogUtilImpl;
import com.summit.service.user.UserService;
import com.summit.util.DateUtil;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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
    	 LogBean logBean =new  LogBean();
    	 try {
    	    logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss",new Date()));
            ResponseCodeEnum c=us.add(userInfo,key);
            if(c!=null){
            	 return ResultBuilder.buildError(c);
            }
            userInfoCache.setUserInfo(userInfo.getUserName(),userInfo);
            logBean.setActionFlag("1");
        } catch (Exception e) {
        	logBean.setActionFlag("0");
        	logBean.setErroInfo(e.getMessage());
            logger.error("新增用户失败", e);
        }
        userInfo.setPassword(null);
        SummitTools.getLogBean(logBean,"用户管理","新增用户信息："+JSONObject.fromObject(userInfo).toString(),"1");
        logUtil.insertLog(logBean);
        if("0".equals(logBean.getActionFlag())){
        	 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }else{
        	 return ResultBuilder.buildSuccess();
        }
        
    }
    
   
   
    @PostMapping("/addUserinfo")
    @ApiOperation(value = "新增用户---头像上传",  notes = "昵称(name),用户名(userName),密码(password)都是必输项")
    public RestfulEntityBySummit<String> addUserinfo(@ApiParam(value = "用户头像",allowMultiple = true) MultipartFile headPortrait, UserInfo userInfo
    		) {
    	 LogBean logBean =new  LogBean();
    	 try {
    		 if(headPortrait!=null){
    			 String snapshotTime = DateUtil.DTFormat(DateUtil.YMD_HMS1,new Date());
                 String headPicpath = new StringBuilder()
                         .append(SystemUtil.getUserInfo().getCurrentDir())
                         .append(File.separator)
                         .append(MainAction.SnapshotFileName)
                         .append(File.separator)
                         .append(snapshotTime)
                         .append("_Head.jpg")
                         .toString();
    			 String headPicUrl = new StringBuilder()
        				 .append("/")
                         .append(MainAction.SnapshotFileName)
                         .append("/")
                         .append(snapshotTime)
                         .append("_Head.jpg")
                         .toString();
        		FileUtil.writeBytes(headPortrait.getBytes(), headPicpath);
        		userInfo.setHeadPortrait(headPicUrl);
    		 }
    		
    	    logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss",new Date()));
            ResponseCodeEnum c=us.add(userInfo,key);
            if(c!=null){
            	 return ResultBuilder.buildError(c);
            }
            userInfoCache.setUserInfo(userInfo.getUserName(),userInfo);
            logBean.setActionFlag("1");
        } catch (Exception e) {
        	logBean.setActionFlag("0");
        	logBean.setErroInfo(e.getMessage());
            logger.error("新增用户失败", e);
        }
        userInfo.setPassword(null);
        SummitTools.getLogBean(logBean,"用户管理","新增用户信息："+JSONObject.fromObject(userInfo).toString(),"1");
        logUtil.insertLog(logBean);
        if("0".equals(logBean.getActionFlag())){
        	 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }else{
        	 return ResultBuilder.buildSuccess();
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
    	 LogBean logBean =new  LogBean();
    	 logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss",new Date()));
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
            logBean.setActionFlag("1");
        } catch (Exception e) {
            logger.error("删除用户信息", e);
            logBean.setActionFlag("0");
        	logBean.setErroInfo(e.getMessage());
        }
        SummitTools.getLogBean(logBean,"用户管理","删除用户:"+userNames,"3");
        logUtil.insertLog(logBean);
        if("0".equals(logBean.getActionFlag())){
        	 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }else{
        	 return ResultBuilder.buildSuccess();
        }
        
    }

    @ApiOperation(value = "修改用户",  notes = "昵称(name)，用户名(userName),密码(password)都是必输项")
    @PostMapping("/edit")
    public RestfulEntityBySummit<String> edit(@RequestBody UserInfo userInfo) {
    	 LogBean logBean =new  LogBean();
    	 logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss",new Date()));
        try {
            ResponseCodeEnum c=us.edit(userInfo,key);
            if(c!=null){
            	 return ResultBuilder.buildError(c);
            }
            userInfoCache.setUserInfo(userInfo.getUserName(),userInfo);
            logBean.setActionFlag("1");
        } catch (Exception e) {
            logger.error("修改用户失败:", e);
            logBean.setActionFlag("0");
        	logBean.setErroInfo(e.getMessage());
        }
        SummitTools.getLogBean(logBean,"用户管理","修改用户:"+JSONObject.fromObject(userInfo).toString(),"2");
        logUtil.insertLog(logBean);
        if("0".equals(logBean.getActionFlag())){
        	 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }else{
        	 return ResultBuilder.buildSuccess();
        }
    }


    @ApiOperation(value = "修改密码",  notes = "旧密码和新密码必须是加密后的数据")
    @PutMapping("/editPassword")
    public RestfulEntityBySummit<String> editPassword(@RequestBody UserPassWordInfo userPassWordInfo) {
    	 LogBean logBean =new  LogBean();
    	 logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss",new Date()));
        try {
            if(!userPassWordInfo.getPassword().equals(userPassWordInfo.getRepeatPassword())){
                return ResultBuilder.buildError(ResponseCodeEnum.CODE_4013);
            }
            ResponseCodeEnum ub=us.editPassword(userPassWordInfo.getUserName(),userPassWordInfo.getOldPassword(), userPassWordInfo.getPassword(), userPassWordInfo.getRepeatPassword(),key);
            if(ub!=null){
            	return ResultBuilder.buildError(ub);
            }
            logBean.setActionFlag("1");
        } catch (Exception e) {
            logger.error("修改密码失败:", e);
            logBean.setActionFlag("0");
        	logBean.setErroInfo(e.getMessage());
        }
        SummitTools.getLogBean(logBean,"用户管理","修改密码:"+JSONObject.fromObject(userPassWordInfo).toString(),"2");
        logUtil.insertLog(logBean);
        if("0".equals(logBean.getActionFlag())){
        	 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }else{
        	 return ResultBuilder.buildSuccess();
        }
    }

   // public ResponseCodeEnum editImei(String imei,String username)
    

    @ApiOperation(value = "根据用户名修改移动设备识别码")
    @PutMapping("/editImei")
    public RestfulEntityBySummit<String> editImei(@RequestBody UserInfo userInfo) {
    	 LogBean logBean =new  LogBean();
    	 logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss",new Date()));
        try {
            ResponseCodeEnum ub=us.editImei(userInfo.getUserName(),userInfo.getImei());
            if(ub!=null){
            	return ResultBuilder.buildError(ub);
            }
            logBean.setActionFlag("1");
        } catch (Exception e) {
            logger.error("修改移动设备识别码失败:", e);
            logBean.setActionFlag("0");
        	logBean.setErroInfo(e.getMessage());
        }
        SummitTools.getLogBean(logBean,"用户管理","根据用户名修改移动设备识别码:"+userInfo.getImei(),"2");
        logUtil.insertLog(logBean);
        if("0".equals(logBean.getActionFlag())){
        	 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }else{
        	 return ResultBuilder.buildSuccess();
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
    
    
    @ApiOperation(value = "根据用户名查询权限菜单")
    @GetMapping("/queryFunctionInfoByUserName")
    public RestfulEntityBySummit<List<FunctionBean>> queryFunctionInfoByUserName(
    		@RequestParam(value = "userName")  String userName) {
        try {
        	
        	UserInfo ub = UserContextHolder.getUserInfo();
            if (ub == null) {
            	 return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
            }
            boolean isSuroleCode=false; 
            if(ub.getRoles()!=null && ub.getRoles().length>0){
            	isSuroleCode=Arrays.asList(ub.getRoles()).contains(SysConstants.SUROLE_CODE);
            }
            List<FunctionBean> funList =us.getFunInfoByUserName(userName,isSuroleCode);
           
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
            @RequestParam(value = "phone",required = false) String phone,
            @RequestParam(value = "state",required = false) String state,
            @RequestParam(value = "adcd",required = false) String adcd,
            @RequestParam(value = "deptName",required = false) String deptName,
            @RequestParam(value = "deptId",required = false) String deptId) {
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
            if(!SummitTools.stringIsNull(adcd)){
                paramJson.put("adcd",adcd);
            }
            if(!SummitTools.stringIsNull(deptId)){
                paramJson.put("deptId",deptId);
            }
            if(!SummitTools.stringIsNull(phone)){
                paramJson.put("phone",phone);
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
    		@RequestParam(value = "userName") String userName,
    		@RequestParam(value = "password") String password) {
    	 LogBean logBean =new  LogBean();
    	 logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss",new Date()));
        try {
        	us.resetPassword(userName,password,key);
        	logBean.setActionFlag("1");
        } catch (Exception e) {
            logger.error("重置密码失败：", e);
            logBean.setActionFlag("0");
        }
        SummitTools.getLogBean(logBean,"用户管理","重置密码:用户名 "+userName,"2");
        logUtil.insertLog(logBean);
        if("0".equals(logBean.getActionFlag())){
        	 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }else{
        	 return ResultBuilder.buildSuccess();
        }
    }

    @ApiOperation(value = "根据用户名查询角色")
    @GetMapping("/queryRoleByUserName")
    public RestfulEntityBySummit<List<String>> queryRoleByUserName(
    		@RequestParam(value = "userName") String userName) {
        try {
        	UserInfo ub = UserContextHolder.getUserInfo();
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
	        	UserInfo ub = UserContextHolder.getUserInfo();;
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
    	 LogBean logBean =new  LogBean();
    	 logBean.setStime(DateUtil.DTFormat("yyyy-MM-dd HH:mm:ss",new Date()));
        try {
        	UserInfo ub = UserContextHolder.getUserInfo();
            if (ub == null) {
            	return ResultBuilder.buildError(ResponseCodeEnum.CODE_4023);
            }
            us.grantRole(userName,role);
            logBean.setActionFlag("1");
        } catch (Exception e) {
        	logBean.setActionFlag("0");
        	logBean.setErroInfo(e.getMessage());
            logger.error("授权权限失败：", e);
            return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }
        SummitTools.getLogBean(logBean,"用户管理","授权权限："+userName+",角色信息:"+role,"4");
        logUtil.insertLog(logBean);
        if("0".equals(logBean.getActionFlag())){
        	 return ResultBuilder.buildError(ResponseCodeEnum.CODE_9999);
        }else{
        	 return ResultBuilder.buildSuccess();
        }
    }

 
}
