package com.summit.service.user;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

import com.summit.service.BaseService;

import net.sf.json.JSONObject;

/**
 * 
 * @author yt
 *
 */
public interface UserService extends BaseService{

	/**
	 * 用户新增方法
	 * @param jsonObject
	 * @return
	 */
	public Map<String, Object> addUser(JSONObject jsonObject);

	/**
	 * 用户修改方法
	 * @param jsonObject
	 * @return
	 */
	public Map<String, Object> editUser(JSONObject jsonObject);

	/**
	 * 删除用户方法
	 * @param username
	 * @return
	 */
	public Map<String, Object> deleteUser(String username);

	/**
	 * 查询用户方法
	 * @param username
	 * @return
	 */
	public Map<String, Object> queryUser(String username);

	/**
	 * 查询用户分页方法
	 * @param currentPage
	 * @param pageSize
	 * @param willPage
	 * @param jsonObject
	 * @return
	 */
	public Map<String, Object> queryPageUser(Integer currentPage, Integer pageSize, Integer willPage, JSONObject jsonObject);

	/**
	 * 修改密码方法
	 * @param oldPassword
	 * @param password
	 * @param repeatPassword
	 * @return
	 */
	public Map<String, Object> editPwdUser(String oldPassword, String password, String repeatPassword);

	/**
	 * 查询用户权限方法
	 * @param userName
	 * @return
	 */
	public Map<String, Object> queryRoleByUserName(String userName);
	
	/**
	 * 重置密码
	 * @param userName
	 * @return
	 */
	public Map<String, Object> resetPassword(String userName);
	
	/**
	 * 删除用户权限方法
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> delUserRoleByUserName(String name) throws Exception;
	
	/**
	 * 权限
	 * @param userName
	 * @return
	 * @return
	 */
	public Collection<GrantedAuthority> getGrantedAuthoritiesByUserName(String userName);

	/**
	 * 授予权限
	 * @param userName
	 * @param role
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> grantRole(String userName, String role) throws Exception;

	/**
	 * 查询用户信息
	 * @param userName
	 * @return
	 */
	Map<String, Object> queryUserByLogin(String userName);
	
	
}
