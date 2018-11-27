package com.summit.service.function;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.access.ConfigAttribute;

import com.summit.service.BaseService;

import net.sf.json.JSONObject;
/**
 * 
 * @author yt
 *
 */
public interface FunctionService  extends BaseService{
	/**
	 * 新增
	 * @param json
	 * @return
	 */
	public Map<String, Object> add(JSONObject json);
	/**
	 * 删除
	 * @param ids
	 * @return
	 */
	public Map<String, Object> del(String ids);
	/**
	 * 修改
	 * @param json
	 * @return
	 */
	public Map<String, Object> edit(JSONObject json);
	/**
	 * 根据id查询
	 * @param id
	 * @return
	 */
	public Map<String, Object> queryById(String id);
	/**
	 * 查询所有
	 * @return
	 */
	
	public Map<String, Object> queryAll();

	/**
	 * 分页查询
	 * @param currentPage
	 * @param pageSize
	 * @param willJumpPage
	 * @param pId
	 * @return
	 */
	public Map<String, Object> queryByPage(Integer currentPage, Integer pageSize, Integer willJumpPage, String pId);
	/**
	 * 根据用户名查询功能
	 * @param userName
	 * @return
	 */
	public String getFunByUserName(String userName) ;

	/**
	 * 查询树
	 * @return
	 */
	public Map<String, Collection<ConfigAttribute>> getResourceMap();
}
