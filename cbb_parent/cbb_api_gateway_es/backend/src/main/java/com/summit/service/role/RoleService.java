package com.summit.service.role;

import java.util.List;
import java.util.Map;

import com.summit.service.BaseService;

import net.sf.json.JSONObject;

/**
 * 
 * @author yt
 *
 */
public interface RoleService  extends BaseService{

	/**
	 * 添加权限
	 * @param jsonObject
	 * @return
	 */
	public Map<String, Object> addRole(JSONObject jsonObject);
	
	/**
	 * 修改权限
	 * @param jsonObject
	 * @return
	 */
	public Map<String, Object> editRole(JSONObject jsonObject);
	/**
	 * 删除权限
	 * @param codes
	 * @return
	 */
	public Map<String, Object> deleteRole(String codes);
	
	/**
	 * 根据权限code查询权限
	 * @param codes
	 * @return
	 */
	public Map<String, Object> queryRoleByCodes(String codes);
	/**
	 * 分页查询
	 * @param currentPage
	 * @param pageSize
	 * @param willJumpPage
	 * @param jsonObject
	 * @return
	 */
	public Map<String, Object> queryByPage(Integer currentPage, Integer pageSize, Integer willJumpPage, JSONObject jsonObject);
	
	/**
	 * 查询所有
	 * @return
	 */
	public Map<String, Object> queryAll() ;

	/**
	 * 根据roleCode查询功能
	 * @param roleCode
	 * @return
	 */
	public List<String> queryFunIdByRoleCode(String roleCode) ;
	/**
	 * 根据roleCode删除权限认证
	 * @param roleCodes
	 * @return
	 */
	public Map<String, Object> delRoleAuthorizationByRoleCode(String roleCodes) ;
	/**
	 * 授权
	 * @param roleCode
	 * @param funIds
	 * @return
	 */
	public Map<String, Object> roleAuthorization(String roleCode, String funIds);

	/**
	 * 给多个用户授权
	 * @param roleCode
	 * @param userName
	 * @return
	 */
	Map<String, Object> roleGrantUser(String roleCode, String userName);
}