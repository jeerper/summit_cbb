package com.summit.controller;

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.common.entity.RestfulEntityBySummit;
import com.summit.common.entity.UserInfo;
import com.summit.common.redis.user.UserInfoCache;
import com.summit.domain.log.LogBean;
import com.summit.service.log.ILogUtil;
import com.summit.service.log.LogUtilImpl;
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    @ApiOperation(value = "新增用户", notes = "用于application/json格式")
    public RestfulEntityBySummit<?> add(UserInfo userInfo, HttpServletRequest request) {
        LogBean logBean = new LogBean();
        try {
            logBean = logUtil.insertLog(request, "1", "用户新增", userInfo.getUserName());
            return new RestfulEntityBySummit<>(us.add(userInfo));
        } catch (Exception e) {
            e.printStackTrace();
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
    public RestfulEntityBySummit<?> del(String userNames, HttpServletRequest request) {
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
            e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
        }
    }

    @ApiOperation(value = "修改用户", notes = "用于application/json格式")
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
            e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
        }
    }

    @ApiOperation(value = "修改密码")
    @PutMapping("/editPassword")
    public RestfulEntityBySummit<?> editPassword(String oldPassword, String password, String repeatPassword,
                                            HttpServletRequest request, String userName) {
        LogBean logBean = new LogBean();
        try {
            logBean = logUtil.insertLog(request, "1", "修改密码", userName);
           return  new RestfulEntityBySummit<>(us.editPassword(userName,oldPassword, password, repeatPassword));
        } catch (Exception e) {
            e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
        }
    }

    @ApiOperation(value = "根据用户名查询用户信息")
    @GetMapping("/queryUserInfoByUserName")
    public RestfulEntityBySummit<?> queryUserInfoByUserName(String userName, HttpServletRequest request) {
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
           
            RestfulEntityBySummit<?> info=new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,ub);
            logger.debug("数据查询成功！"+info.toString()); 
            return info;
        } catch (Exception e) {
        	logger.debug("数据查询失败1！" +e.toString());
            e.printStackTrace();
            logger.debug("数据查询失败2！" +e.toString());
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            //return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
            return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9993);
        }
    }
    
    @ApiOperation(value = "根据用户名查询所有菜单")
    @GetMapping("/queryFunctionInfoByUserName")
    public RestfulEntityBySummit<?> queryFunctionInfoByUserName(String userName, HttpServletRequest request) {
        LogBean logBean = new LogBean();
        try {
            logBean = logUtil.insertLog(request, "1", "用户管理根据用户名查询菜单信息", userName);
            List<JSONObject> funList=us.getFunInfoByUserName(userName);
            if (funList == null) {
            	return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_4023);
            }
            return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,funList);
        }catch (Exception e) {
            e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
        }
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/queryByPage")
    public RestfulEntityBySummit<Page<JSONObject>> queryByPage(Integer start, Integer limit, UserInfo userInfo, HttpServletRequest request) {
        LogBean logBean = new LogBean();
        try {
            logBean = logUtil.insertLog(request, "1", "用户管理分页查询", "");
            start = (start == null) ? 1 : start;
            limit = (limit == null) ? SysConstants.PAGE_SIZE : limit;
            Page<JSONObject> page=us.queryByPage(start, limit, userInfo);
            return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,page);
        } catch (Exception e) {
            e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
           return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999,null);
        }
    }

    @ApiOperation(value = "重置密码")
    @PutMapping("/resetPassword")
    public RestfulEntityBySummit<?> resetPassword(String userName, HttpServletRequest request) {
        LogBean logBean = new LogBean();
        try {
            logBean = logUtil.insertLog(request, "1", "用户管理重置密码", userName);
//            if (st.stringEquals(SysConstants.SUPER_USERNAME, userName)) {
//            	return new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000);
//            } else {
            	return new RestfulEntityBySummit<>(us.resetPassword(userName));
//            }
        } catch (Exception e) {
            e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");
            return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
        }
    }

    @ApiOperation(value = "根据用户名查询角色")
    @GetMapping("/queryRoleByUserName")
    public RestfulEntityBySummit<?> queryRoleByUserName(String userName, HttpServletRequest request) {
        LogBean logBean = new LogBean();
        try {
        	List<String> list=us.queryRoleByUserName(userName);
            logBean = logUtil.insertLog(request, "1", "用户管理查询用户角色", userName);
//            if (st.stringEquals(SysConstants.SUPER_USERNAME, userName)) {
//            	return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000);
//            } else {
            return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_0000,list);
//            }
        } catch (Exception e) {
            e.printStackTrace();
            logBean.setActionFlag("0");
            logBean.setErroInfo(e.toString());
            logUtil.updateLog(logBean, "1");     
            return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
        }
    }

    @ApiOperation(value = "授权权限")
    @PutMapping("/grantRole")
    public RestfulEntityBySummit<?>  grantRole(String userName, String role, HttpServletRequest request) {
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
            return  new RestfulEntityBySummit<>(ResponseCodeBySummit.CODE_9999);
        }
    }

}
