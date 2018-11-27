package com.summit.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.summit.domain.UserEnum;
import com.summit.service.user.UserService;
import com.summit.util.SummitTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.sf.json.JSONObject;

/**
 * 
 * @author yt
 *
 */
@RestController
@Api("用户模块")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user")
public class UserController {


	@Autowired
	UserService userService;
	
	@Autowired 
	SummitTools st;
	
	@GetMapping("/user")
    public Principal user(Principal user){
        return user;
    }
	
	
	@ApiOperation(value="新增用户",notes="用于application/json格式")
	@RequestMapping(value="/add", method = RequestMethod.POST)
	public Object addUser(@ApiParam(value = "用戶信息example:{\"USERNAME\":\"zhangsan\",\"PASSWORD\":\"888888\",\"EMAIL\":\"@qq.com\",\"NAME\":\"zhangsan\",\"PHONE_NUMBER\":\"123456\",\"NOTE\": \"备注 \"}", required = true) @RequestBody JSONObject jsonObject){
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
        String password = jsonObject.getString(UserEnum.PASSWORD.toString());
        jsonObject.put(UserEnum.PASSWORD.toString(), encoder.encode(password.trim()));
		return userService.addUser(jsonObject);
	}
	
	@ApiOperation(value="修改用户",notes="用于application/json格式")
	@RequestMapping(value="/edit", method = RequestMethod.PUT)
	public Object editUser(@ApiParam(value = "用戶信息example:{\"USERNAME\":\"zhangsan\",\"EMAIL\":\"@qq.com\",\"NAME\":\"zhangsan\",\"PHONE_NUMBER\":\"123456\",\"NOTE\": \"备注 \"}", required = true) @RequestBody JSONObject jsonObject){
		return userService.editUser(jsonObject);
	}
	
	@ApiOperation(value = "删除用户信息")
	
	/**
	 *  //@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	 * @param username
	 * @return
	 */
	@RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
	public Object deleteUser(@ApiParam(value="用户名",required = true) @PathVariable(name = "username") String username){
		return 	userService.deleteUser(username);
	}
	
	/**
	 *  //@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	 *	//@PreAuthorize("hasAnyAuthority('ROLE_DEFAULT')")
	 * @param username
	 * @return
	 */
	@ApiOperation(value = "根据用户名查询用户信息")
	//@PreAuthorize("hasAnyAuthority('SUPER_ADMIN')")
	@RequestMapping(value = "/{username}", method = RequestMethod.POST)
	public Object queryUser(@ApiParam(value="用户名",required = true) @PathVariable(name = "username") String username){		
		
		return userService.queryUser(username);
	}
	
	@ApiOperation(value = "修改密码")
	@RequestMapping(value="/editPwd",method = RequestMethod.PUT)
	public Object editPassword(@ApiParam(value = "旧密码", required = true)@RequestParam(name = "oldPassword") String oldPassword
			,@ApiParam(value = "新密码", required = true)@RequestParam(name = "password") String password
			,@ApiParam(value = "确认密码", required = true)@RequestParam(name = "repeatPassword") String repeatPassword
			){

		return userService.editPwdUser(oldPassword, password, repeatPassword);
	}
	@ApiOperation(value = "重置密码")
	@RequestMapping(value="/resetPwd",method = RequestMethod.PUT)
	public Object resetPassword(@ApiParam(value = "用户名" ,required = true)@RequestParam(name = "username") String username){
		
		return userService.resetPassword(username);
	}
	
	@ApiOperation(value = "分页查询")
	@RequestMapping(value="/queryPage",method = RequestMethod.POST)
	public Object queryPage(@ApiParam(value="当前页",required = false)@RequestParam(name="currentPage") Integer currentPage
			,@ApiParam(value="每页大小",required = false)@RequestParam(name="pageSize") Integer pageSize
			,@ApiParam(value="跳转页数",required = false)@RequestParam(name="willJumpPage") Integer willJumpPage
			,@ApiParam(value = "用戶信息example:{\"USERNAME\":\"zhangsan\",\"NAME\":\"zhangsan\",\"STATE\":\"1\",\"IS_ENABLED\": \"1\"}", required = true) @RequestBody JSONObject jsonObject
			){
		return 	userService.queryPageUser(currentPage, pageSize, willJumpPage, jsonObject);
	}
	
	@ApiOperation(value = "根据用户名查询权限")
	@RequestMapping(value="/queryRole",method = RequestMethod.POST)
	public  Object queryRole(@ApiParam(value = "用户名" ,required = true)@RequestParam(name = "username") String username){
		
		return  userService.queryRoleByUserName(username);
	}
	
	@ApiOperation(value = "授权权限")
	@RequestMapping(value="/grantRole",method = RequestMethod.POST)
	public Object grantRole(@ApiParam(value = "用户名" ,required = true)@RequestParam(name = "username") String username
			,@ApiParam(value = "权限" ,required = true)@RequestParam(name = "roleCode") String roleCode){
		Map<String, Object> js = null;
		try {
			js  =   userService.grantRole(username, roleCode);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return st.error("授权失败", "");
		}
		return js ;
	}
	
}
