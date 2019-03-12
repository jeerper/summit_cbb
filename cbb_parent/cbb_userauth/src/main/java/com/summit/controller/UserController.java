package com.summit.controller;

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.UserInfo;
import com.summit.common.redis.user.UserInfoCache;
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

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    private SummitTools st;
    @Autowired
    UserInfoCache userInfoCache;

    @PostMapping("/add")
    @ApiOperation(value = "新增用户",  notes = "昵称(name)，用户名(userName),密码(password)都是必输项")
    public RestfulEntityBySummit<?> add(UserInfo userInfo, HttpServletRequest request) {
        LogBean logBean = new LogBean();
        try {
            logBean = logUtil.insertLog(request, "1", "用户新增", userInfo.getUserName());
            return new RestfulEntityBySummit<>(us.add(userInfo));
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("新增用户失败", e);
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
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
    public RestfulEntityBySummit<?> del(
    		@RequestParam(value = "userNames") String userNames, HttpServletRequest request) {
        LogBean logBean = new LogBean();
        try {
            logBean = logUtil.insertLog(request, "1", "删除用户", "");
            if(userNames.contains(",")){
            	for(String username:userNames.split(",")){
            		//系统管路员用户不能删除
            		if (st.stringEquals(SysConstants.SUPER_USERNAME, username)) {
            			continue;
                    }
            		userInfoCache.deleteUserInfo(username);		
            	}
            }
            return new RestfulEntityBySummit<>(us.del(userNames));
        } catch (Exception e) {
            //e.printStackTrace();
            logger.error("删除用户信息", e);
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
        }
    }

    @ApiOperation(value = "修改用户",  notes = "昵称(name)，用户名(userName),密码(password)都是必输项")
    @PutMapping("/edit")
    public RestfulEntityBySummit<?> edit(UserInfo userInfo, HttpServletRequest request) {
        LogBean logBean = new LogBean();
        try {
            logBean = logUtil.insertLog(request, "1", "修改用户", "");
//            if (st.stringEquals(SysConstants.SUPER_USERNAME, userInfo.getUserName())) {
//            	 return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000);
//            } else {
            	userInfoCache.setUserInfo(userInfo.getUserName(),userInfo);
            	return new RestfulEntityBySummit<>(us.edit(userInfo));
//            }
        } catch (Exception e) {
            //e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            logger.error("修改用户失败:", e);
            return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
        }
    }

    @ApiOperation(value = "修改密码")
    @PutMapping("/editPassword")
    public RestfulEntityBySummit<?> editPassword(
    		@RequestParam(value = "oldPassword")  String oldPassword,
    		@RequestParam(value = "password")  String password, 
    		@RequestParam(value = "repeatPassword")  String repeatPassword,
    		@RequestParam(value = "userName")  String userName,
            HttpServletRequest request) {
        LogBean logBean = new LogBean();
        try {
            logBean = logUtil.insertLog(request, "1", "修改密码", userName);
           return  new RestfulEntityBySummit<>(us.editPassword(userName,oldPassword, password, repeatPassword));
        } catch (Exception e) {
            //e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            logger.error("修改密码失败:", e);
            return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
        }
    }

    @ApiOperation(value = "根据用户名查询用户信息")
    @GetMapping("/queryUserInfoByUserName")
    public RestfulEntityBySummit<?> queryUserInfoByUserName(
    		@RequestParam(value = "userName")  String userName, HttpServletRequest request) {
        LogBean logBean = new LogBean();
        try {
            logBean = logUtil.insertLog(request, "1", "用户管理根据用户名查询用户", userName);
            UserInfo ub = us.queryByUserName(userName);
            if (ub == null) {
            	return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_4023);
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
//            JSONArray jsonArray = new JSONArray();
//            jsonArray.put(ub);
            RestfulEntityBySummit<?> info=new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,ub);
            //logger.debug("数据查询成功！"+info.getCode()+"==="+info.getData()); 
            return info;
        } catch (Exception e) {
        	//logger.debug("数据查询失败1！" +e.toString());
            //e.printStackTrace();
            //logger.debug("数据查询失败2！" +e.toString());
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            logger.error("根据用户名查询用户信息失败：", e);
            //return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
            return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9993);
        }
    }
    
    @ApiOperation(value = "根据用户名查询所有菜单")
    @GetMapping("/queryFunctionInfoByUserName")
    public RestfulEntityBySummit<?> queryFunctionInfoByUserName(
    		@RequestParam(value = "userName")  String userName, HttpServletRequest request) {
        LogBean logBean = new LogBean();
        try {
            logBean = logUtil.insertLog(request, "1", "用户管理根据用户名查询菜单信息", userName);
            List<JSONObject> funList=us.getFunInfoByUserName(userName);
            if (funList == null) {
            	return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_4023);
            }
            //JSONArray jsonArray = new JSONArray();
            //jsonArray.put(funList);
            return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,funList);
        }catch (Exception e) {
            //e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            logger.error("根据用户名查询所有菜单失败：", e);
            return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
        }
    }

    @ApiOperation(value = "分页查询")
    @PostMapping("/queryByPage")
    public RestfulEntityBySummit<?> queryByPage(
    		@RequestParam(value = "page") int page,
            @RequestParam(value ="pageSize") int pageSize,
            @RequestParam(value = "name",required = false) String name,
            @RequestParam(value = "userName",required = false) String userName,
            @RequestParam(value = "isEnabled",required = false) String isEnabled,
            @RequestParam(value = "state",required = false) String state,HttpServletRequest request) {
        LogBean logBean = new LogBean();
        try {
            logBean = logUtil.insertLog(request, "1", "用户管理分页查询", "");
            page = (page == 0) ? 1 : page;
            pageSize = (pageSize == 0) ? SysConstants.PAGE_SIZE : pageSize;
            
            JSONObject paramJson = new JSONObject();
            if(!st.stringIsNull(name)){
                paramJson.put("name",name);
            }
            if(!st.stringIsNull(userName)){
                paramJson.put("userName",userName);
            }
            if(!st.stringIsNull(isEnabled)){
                paramJson.put("isEnabled",isEnabled);
            }
            if(!st.stringIsNull(state)){
                paramJson.put("state",state);
            }
            Page<JSONObject> pageList=us.queryByPage(page, pageSize, paramJson);
            //JSONArray jsonArray = new JSONArray();
            //jsonArray.put(page);
            return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,pageList);
        } catch (Exception e) {
            //e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            logger.error("用户分页查询失败：", e);
           return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999,null);
        }
    }

    @ApiOperation(value = "重置密码")
    @PutMapping("/resetPassword")
    public RestfulEntityBySummit<?> resetPassword(
    		@RequestParam(value = "userName") String userName, HttpServletRequest request) {
        LogBean logBean = new LogBean();
        try {
            logBean = logUtil.insertLog(request, "1", "用户管理重置密码", userName);
//            if (st.stringEquals(SysConstants.SUPER_USERNAME, userName)) {
//            	return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000);
//            } else {
            	return new RestfulEntityBySummit<>(us.resetPassword(userName));
//            }
        } catch (Exception e) {
            //e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            logger.error("重置密码失败：", e);
            return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
        }
    }

    @ApiOperation(value = "根据用户名查询角色")
    @GetMapping("/queryRoleByUserName")
    public RestfulEntityBySummit<?> queryRoleByUserName(
    		@RequestParam(value = "userName") String userName, HttpServletRequest request) {
        LogBean logBean = new LogBean();
        try {
        	List<String> list=us.queryRoleByUserName(userName);
            logBean = logUtil.insertLog(request, "1", "用户管理查询用户角色", userName);
//            if (st.stringEquals(SysConstants.SUPER_USERNAME, userName)) {
//            	return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000);
//            } else {
            //JSONArray jsonArray = new JSONArray();
           // jsonArray.put(list);
            return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,list);
//            }
        } catch (Exception e) {
            //e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1"); 
            logger.error("根据用户名查询角色失败：", e);
            return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
        }
    }

    @ApiOperation(value = "授权权限")
    @PutMapping("/grantRole")
    public RestfulEntityBySummit<?>  grantRole(
    		@RequestParam(value = "userName") String userName,
    		@RequestParam(value = "role") String role, HttpServletRequest request) {
        LogBean logBean = new LogBean();
        try {
            logBean = logUtil.insertLog(request, "1", "用户管理授权", userName);
//            if (st.stringEquals(SysConstants.SUPER_USERNAME, userName)) {
//            	return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000);
//            } else {
            	return  new RestfulEntityBySummit<>(us.grantRole(userName,role));
//            }
        } catch (Exception e) {
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            logger.error("授权权限失败：", e);
            return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
        }
    }

}
