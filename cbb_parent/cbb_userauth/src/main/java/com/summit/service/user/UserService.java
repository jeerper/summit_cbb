package com.summit.service.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.FunctionBean;
import com.summit.common.entity.UserInfo;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.common.entity.UserInfo;
import com.summit.repository.UserRepository;
import com.summit.util.SummitTools;
import com.summit.util.SysConstants;

import net.sf.json.JSONObject;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.CharsetUtil;
@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository ur;
	@Autowired
	private SummitTools st;
	@Autowired
	public JdbcTemplate jdbcTemplate;

	private static final String KEY_ALGORITHM = "AES";
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/CBC/NOPadding";
    
   

	@Transactional
	public ResponseCodeEnum add(UserInfo userInfo,String key) {
		//保存用户
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
		String sql = "SELECT * FROM SYS_USER WHERE USERNAME = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, userInfo.getUserName());
		if (st.collectionNotNull(l)) {
			return ResponseCodeEnum.CODE_4022;
		}
		if(userInfo.getIsEnabled()==null){
			userInfo.setIsEnabled(1);
		}
		if(userInfo.getPassword()!=null && !"".equals(userInfo.getPassword())){
			 //解密
	        try {
	        	userInfo.setPassword(decryptAES(userInfo.getPassword(), key).trim());
	        } catch (Exception e) {
	        	return ResponseCodeEnum.CODE_4014;
	        }	
		}
		sql = "INSERT INTO SYS_USER (USERNAME,NAME,SEX,PASSWORD,IS_ENABLED,EMAIL,PHONE_NUMBER,STATE,NOTE,LAST_UPDATE_TIME) VALUES ( ?,?, ?, ?, ?, ?, ?, ?, ?,now())";
		jdbcTemplate.update(sql,
				userInfo.getUserName(),
				userInfo.getName(),
				userInfo.getSex(),
				encoder.encode(userInfo.getPassword()),
				userInfo.getIsEnabled(),
				userInfo.getEmail(),
				userInfo.getPhoneNumber(),
				1,
				userInfo.getNote()
				);
		
		//保存行政区划
		if(userInfo.getAdcds()!=null && userInfo.getAdcds().length>0){
			String insertAdcdSql="INSERT INTO SYS_USER_ADCD(ID,USERNAME,ADCD,CREATETIME) VALUES ( ?, ?, ?, now())";
			List userAdcdParams = new ArrayList();
	        for(String adcd:userInfo.getAdcds()){
	            Object glxxParam[]={
	            		SummitTools.getKey(),
	            		userInfo.getUserName(),
	            		adcd,
	            };
	            userAdcdParams.add(glxxParam);
	        }
	        jdbcTemplate.batchUpdate(insertAdcdSql,userAdcdParams);
		}
		//保存部门
		if(userInfo.getDepts()!=null && userInfo.getDepts().length>0){
			String insertAdcdSql="INSERT INTO SYS_USER_DEPT(ID,USERNAME,DEPTID,CREATETIME) VALUES ( ?, ?, ?, now())";
			List userAdcdParams = new ArrayList();
	        for(String deptId:userInfo.getDepts()){
	            Object glxxParam[]={
	            		SummitTools.getKey(),
	            		userInfo.getUserName(),
	            		deptId,
	            };
	            userAdcdParams.add(glxxParam);
	        }
	        jdbcTemplate.batchUpdate(insertAdcdSql,userAdcdParams);
		}
		
		return null;
	}



	@Transactional
	public ResponseCodeEnum edit(UserInfo userInfo,String key) {
		if(userInfo.getPassword()!=null && !"".equals(userInfo.getPassword())){
			 //解密
	        try {
	        	userInfo.setPassword(decryptAES(userInfo.getPassword(), key).trim());
	        } catch (Exception e) {
	        	return ResponseCodeEnum.CODE_4014;
	        }	
		}
		String sql = "UPDATE SYS_USER SET NAME = ?,SEX=?, EMAIL = ?, PHONE_NUMBER =?, NOTE = ?, IS_ENABLED = ?, LAST_UPDATE_TIME = now() WHERE USERNAME = ? AND STATE = 1";
		jdbcTemplate.update(sql, userInfo.getName(), userInfo.getSex(), userInfo.getEmail(),
				userInfo.getPhoneNumber(), userInfo.getNote(), userInfo
						.getIsEnabled(), userInfo.getUserName());
		
		String adcdSql=" delete from sys_user_adcd where USERNAME  IN ('"+userInfo.getUserName()+"') ";
		jdbcTemplate.update(adcdSql);
		
		//保存行政区划
		if(userInfo.getAdcds()!=null && userInfo.getAdcds().length>0){
			String insertAdcdSql="INSERT INTO SYS_USER_ADCD(ID,USERNAME,ADCD,CREATETIME) VALUES ( ?, ?, ?, now())";
			List userAdcdParams = new ArrayList();
			for(String adcd:userInfo.getAdcds()){
			            Object adcdParam[]={
			            		SummitTools.getKey(),
			            		userInfo.getUserName(),
			            		adcd,
			            };
			       userAdcdParams.add(adcdParam);
			}
			jdbcTemplate.batchUpdate(insertAdcdSql,userAdcdParams);
		}
		
		String deptSql=" delete from SYS_USER_DEPT where USERNAME  IN ('"+userInfo.getUserName()+"') ";
		jdbcTemplate.update(deptSql);
		//保存部门
		if(userInfo.getDepts()!=null && userInfo.getDepts().length>0){
			String insertAdcdSql="INSERT INTO SYS_USER_DEPT(ID,USERNAME,DEPTID,CREATETIME) VALUES ( ?, ?, ?, now())";
			List userdeptParams = new ArrayList();
	        for(String deptId:userInfo.getDepts()){
	            Object deptParam[]={
	            		SummitTools.getKey(),
	            		userInfo.getUserName(),
	            		deptId,
	            };
	            userdeptParams.add(deptParam);
	        }
	        jdbcTemplate.batchUpdate(insertAdcdSql,userdeptParams);
		}
		return null;
	}

	public ResponseCodeEnum editImei(String username,String imei) {
		String sql = "UPDATE SYS_USER SET imei = ? WHERE USERNAME = ? AND STATE = 1";
		jdbcTemplate.update(sql, imei, username);
		return null;
	}
	
	
	public ResponseCodeEnum editPassword(String userName, String oldPassword,
			String password, String repeatPassword,String key) throws Exception  {
		
		UserInfo ub = queryByUserName(userName);
        if (ub == null) {
        	 return ResponseCodeEnum.CODE_4023;
        }
        String decodePasswordOld ="";
        //解密
        try {
           decodePasswordOld = decryptAES(oldPassword, key).trim();
        } catch (Exception e) {
        	return ResponseCodeEnum.CODE_4014;
        }
        
        //验证旧密码是否正确
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder(); 
        if(!encoder.matches(decodePasswordOld, ub.getPassword())){
        	 return ResponseCodeEnum.CODE_4009;
        }
        
        try {
			password=decryptAES(password, key).trim();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        //此处需要新密码解密后再加密保存
		String sql = "UPDATE SYS_USER SET PASSWORD = ?, LAST_UPDATE_TIME = ? WHERE USERNAME = ? AND STATE = 1";
		jdbcTemplate.update(sql, encoder.encode(password), new Date(),userName );
		
		return null;
	}
	
	
	public void  del(String userNames) {
		userNames = userNames.replaceAll(",", "','");
		String sql = "UPDATE SYS_USER SET STATE = '0',IS_ENABLED='0', LAST_UPDATE_TIME = ? WHERE USERNAME <> '"+ SysConstants.SUPER_USERNAME+ "' AND USERNAME IN ('"+ userNames + "')";
		jdbcTemplate.update(sql,new Date());
		delUserRoleByUserName(userNames);
		
//		String adcdSql=" delete from sys_user_adcd where USERNAME  IN ('"+userNames+"') ";
//		jdbcTemplate.update(adcdSql);
//		
//		String deptSql=" delete from SYS_USER_DEPT where USERNAME  IN ('"+userNames+"') ";
//		jdbcTemplate.update(deptSql);
	}

	public UserInfo queryByUserName(String userName) throws Exception {
		String sql = "SELECT USERNAME ,NAME ,SEX,PASSWORD ,IS_ENABLED ,EMAIL ,PHONE_NUMBER ,STATE ,NOTE ,LAST_UPDATE_TIME,IMEI FROM SYS_USER WHERE STATE = 1 AND USERNAME = ?";
		LinkedMap linkedMap=new LinkedMap();
		linkedMap.put(1,userName);
		List<Object> dataList = ur.queryAllCustom(sql, linkedMap);
		if(dataList!=null && dataList.size()>0){
			UserInfo userInfo=JSON.parseObject(dataList.get(0).toString(), new TypeReference<UserInfo>() {});	
			return userInfo;
		}
		return null;
	}

	public Page<UserInfo> queryByPage(int start, int limit, JSONObject paramJson) throws Exception {
		LinkedMap linkedMap=new LinkedMap();
		Integer index = 1;
		StringBuilder sb = new StringBuilder(
				"SELECT user1.USERNAME,NAME,SEX,IS_ENABLED,EMAIL,PHONE_NUMBER,STATE,NOTE,USERADCD.ADCD,useradcd.adnms,userdept.DEPTID,userdept.deptNames,date_format(LAST_UPDATE_TIME, '%Y-%m-%d %H:%i:%s') as lastUpdateTime FROM SYS_USER user1 ");
		sb.append(" left join (SELECT username,GROUP_CONCAT(useradcd.`adcd`)AS adcd ,GROUP_CONCAT(`adnm`)AS adnms ");
		sb.append(" FROM sys_user_adcd useradcd inner join sys_ad_cd ad on useradcd.adcd=ad.adcd GROUP BY username)useradcd on useradcd.username=user1.USERNAME");
		sb.append(" left join (SELECT username,GROUP_CONCAT(userdept.`deptid`)AS DEPTID,GROUP_CONCAT(dept.`deptname`)AS deptNames FROM sys_user_dept userdept ");
		sb.append(" inner join sys_dept dept on userdept.deptid=dept.id GROUP BY username)userdept on userdept.username=user1.USERNAME");
		sb.append(" WHERE user1.USERNAME <> '");
		sb.append(SysConstants.SUPER_USERNAME );
		sb.append("'");
		if (paramJson.containsKey("name")) {
			sb.append(" AND NAME LIKE ? ");
			linkedMap.put(index,"%" + paramJson.get("name") + "%");
    		index++;
		}
		if (paramJson.containsKey("userName")) {
			sb.append(" AND user1.USERNAME LIKE ? ");
			linkedMap.put(index,"%" + paramJson.get("userName") + "%");
    		index++;
		}
		if (paramJson.containsKey("isEnabled")) {
			sb.append(" AND IS_ENABLED = ? ");
			linkedMap.put(index, paramJson.get("isEnabled"));
    		index++;
		}
		if (paramJson.containsKey("state")) {
			sb.append(" AND STATE =  ? ");
			linkedMap.put(index, paramJson.get("state"));
    		index++;
		}
		if (paramJson.containsKey("adcd")) {
			sb.append(" AND USERADCD.ADCD like  ? ");
			linkedMap.put(index, "%" + paramJson.get("adcd") + "%");
    		index++;
		}
		
		if (paramJson.containsKey("deptName")) {
			sb.append(" AND dept.detpName  like   ? ");
			linkedMap.put(index,"%" + paramJson.get("deptName") + "%");
    		index++;
		}
		
		if (paramJson.containsKey("deptId")) {
			sb.append(" AND userdept.deptid  =   ? ");
			linkedMap.put(index,paramJson.get("deptId"));
    		index++;
		}
		
		Page<Object> page= ur.queryByCustomPage(sb.toString(), start, limit,linkedMap);
		if(page!=null){
			List<UserInfo> userInfoList=new ArrayList<UserInfo>();
			if(page.getContent()!=null && page.getContent().size()>0){
				  for(Object o:page.getContent()){
					 JSONObject jsonObject=(JSONObject)o;
					 UserInfo userInfo=JSON.parseObject(o.toString(), new TypeReference<UserInfo>() {});
					 userInfo.setAdcds(jsonObject.containsKey("ADCD")?jsonObject.getString("ADCD").split(","):null);
					 userInfo.setDepts(jsonObject.containsKey("DEPTID")?jsonObject.getString("DEPTID").split(","):null);
					 userInfoList.add(userInfo);
				 }
			}
			
			return new Page<UserInfo>(userInfoList,page.getPageable());
		}
		return null;
	} 
	
	

	public List<UserInfo> queryUserInfoList(JSONObject paramJson) throws Exception {
		StringBuilder sb = new StringBuilder(
				"SELECT user1.USERNAME,NAME,SEX,IS_ENABLED,EMAIL,PHONE_NUMBER,STATE,NOTE,USERADCD.ADCD,useradcd.adnms,userdept.DEPTID,userdept.deptNames FROM SYS_USER user1 ");
		sb.append(" left join (SELECT username,GROUP_CONCAT(useradcd.`adcd`)AS adcd ,GROUP_CONCAT(`adnm`)AS adnms ");
		sb.append(" FROM sys_user_adcd useradcd inner join sys_ad_cd ad on useradcd.adcd=ad.adcd GROUP BY username)useradcd on useradcd.username=user1.USERNAME");
		sb.append(" left join (SELECT username,GROUP_CONCAT(userdept.`deptid`)AS DEPTID,GROUP_CONCAT(dept.`deptname`)AS deptNames FROM sys_user_dept userdept ");
		sb.append(" inner join sys_dept dept on userdept.deptid=dept.id  GROUP BY username)userdept on userdept.username=user1.USERNAME");
		sb.append(" WHERE user1.USERNAME <> '");
		sb.append(SysConstants.SUPER_USERNAME );
		sb.append("'");
		if (paramJson.containsKey("name")) {
			sb.append(" AND NAME LIKE '%"+paramJson.get("name")+"%'");
		}
		if (paramJson.containsKey("userName")) {
			sb.append(" AND user1.USERNAME LIKE '%"+paramJson.get("userName")+"%'");
		}
		if (paramJson.containsKey("isEnabled")) {
			sb.append(" AND IS_ENABLED = '"+paramJson.get("isEnabled")+"'");
		}
		if (paramJson.containsKey("state")) {
			sb.append(" AND STATE = '"+paramJson.get("state")+"'");
		}
		List<Object> list= ur.queryAllCustom(sb.toString(), new LinkedMap());
		if(list!=null && list.size()>0){
			List<UserInfo> userInfoList=new ArrayList<UserInfo>();
			  for(Object o:list){
					 JSONObject jsonObject=(JSONObject)o;
					 UserInfo userInfo=JSON.parseObject(o.toString(), new TypeReference<UserInfo>() {});
					 userInfo.setAdcds(jsonObject.containsKey("ADCD")?jsonObject.getString("ADCD").split(","):null);
					 userInfo.setDepts(jsonObject.containsKey("DEPTID")?jsonObject.getString("DEPTID").split(","):null);
					 userInfoList.add(userInfo);
			 }
			 return userInfoList;
		}
		return null;
	}
	
	public void resetPassword(String userNames) {
		userNames = userNames.replaceAll(",", "','");
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
		String sql = "UPDATE SYS_USER SET PASSWORD = ?, LAST_UPDATE_TIME = ? WHERE USERNAME = ?";
		jdbcTemplate.update(sql, encoder.encode(SysConstants.USERNAMEPASSWORD),  new Date(),
				userNames);
	}

	public List<String> queryRoleByUserName(String userName) {
		List<String> list = new ArrayList<String>();
		String sql = "SELECT ROLE_CODE FROM SYS_USER_ROLE WHERE USERNAME = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, userName);
		for (JSONObject o : l) {
			list.add(st.objJsonGetString(o, "ROLE_CODE"));
		}
		return list;
	}
	
	public List<String> queryRoleListByUserName(String userName) {
		List<String> list = new ArrayList<String>();
		String sql = "SELECT ROLE_CODE as 'key' FROM SYS_USER_ROLE WHERE USERNAME = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, userName);
		for (JSONObject o : l) {
			list.add(st.objJsonGetString(o, "key"));
		}
	     return list;
	    
	}
	
	public JSONObject queryAdcdByUserName(String userName) {
		String sql = "SELECT USERADCD.ADCD, ADCDS.ADNM FROM SYS_USER_ADCD USERADCD inner join SYS_AD_CD ADCDS  ON USERADCD.ADCD=ADCDS.ADCD  WHERE USERNAME = ?";
		List<JSONObject> userAdcdList= ur.queryAllCustom(sql, userName);
		if(userAdcdList!=null && userAdcdList.size()>0){
			String adnms="";
			String[] adcds=  new String [userAdcdList.size()];
			int i=0;
			for (JSONObject userAdcdObject : userAdcdList) {
				adcds[i]=st.objJsonGetString(userAdcdObject, "ADCD");
				if(userAdcdObject.containsKey("ADNM")  &&  !st.stringIsNull(userAdcdObject.getString("ADNM"))){
				   adnms+=st.objJsonGetString(userAdcdObject, "ADNM")+",";
				}
				i++;
			}
			JSONObject jsonobject=new JSONObject();
			jsonobject.put("adcds", adcds);
			if(adnms!=null && !"".equals(adnms)){
				jsonobject.put("adnms", adnms.substring(0, adnms.length()-1));	
			}
			return jsonobject;
		}
		return null;
	}
	
	public JSONObject queryDeptByUserName(String userName) {
		String sql = "SELECT DEPTID,DEPTNAME FROM SYS_USER_DEPT userdept inner join sys_dept dept on userdept.DEPTID=dept.ID WHERE USERNAME = ?";
		List<JSONObject> userDeltList = ur.queryAllCustom(sql, userName);
		if(userDeltList!=null && userDeltList.size()>0){
			String deptnames="";
			String[] deptIds=  new String [userDeltList.size()];
			int i=0;
			for (JSONObject userdeptObject : userDeltList) {
				deptIds[i]=st.objJsonGetString(userdeptObject, "DEPTID");
				if(userdeptObject.containsKey("DEPTNAME")  &&  !st.stringIsNull(userdeptObject.getString("DEPTNAME"))){
				   deptnames+=st.objJsonGetString(userdeptObject, "DEPTNAME")+",";
				}
				i++;
			}
			JSONObject jsonobject=new JSONObject();
			jsonobject.put("deptIds", deptIds);
			if(deptnames!=null && !"".equals(deptnames)){
				jsonobject.put("deptnames", deptnames.substring(0, deptnames.length()-1));	
			}
			return jsonobject;
		}
		return null;
	}
	public void delUserRoleByUserName(String userName){
		String sql = "DELETE FROM SYS_USER_ROLE WHERE USERNAME =?";
		jdbcTemplate.update(sql,userName);
	}
	
	@Transactional
	public void grantRole(String userName, String[] role) {
		String deleteSql = "DELETE FROM SYS_USER_ROLE WHERE USERNAME =?";
		jdbcTemplate.update(deleteSql,userName);
		if(role!=null && role.length>0){
			String sql = "INSERT INTO SYS_USER_ROLE (ID, USERNAME, ROLE_CODE) VALUES (?, ?, ?)";
			List<Object[]> batchArgs = new ArrayList<Object[]>();
			for (String roleCode : role) {
				batchArgs.add(new Object[] { SummitTools.getKey(), userName, roleCode });
			}
			jdbcTemplate.batchUpdate(sql, batchArgs);
		}
	}


	public List<String> getFunByUserName(String userName){
		List<String> list = new ArrayList<String>();
		String sql = "SELECT DISTINCT SF.ID FROM SYS_USER_ROLE SUR INNER JOIN SYS_ROLE_FUNCTION SRF ON ( SUR.ROLE_CODE = SRF.ROLE_CODE ) INNER JOIN SYS_FUNCTION SF ON (SRF.FUNCTION_ID = SF.ID) WHERE SF.IS_ENABLED = '1' AND SF.SUPER_FUN = 0 AND SUR.USERNAME = ?";
		List<JSONObject> l = ur.queryAllCustom(sql, userName);
		for (JSONObject o : l) {
			list.add(st.objJsonGetString(o, "ID"));
		}
		return list;
	}

	public List<FunctionBean> getFunInfoByUserName(String userName){
		String sql = "SELECT  SF.* FROM SYS_USER_ROLE SUR INNER JOIN SYS_ROLE_FUNCTION SRF ON ( SUR.ROLE_CODE = SRF.ROLE_CODE ) INNER JOIN SYS_FUNCTION SF ON (SRF.FUNCTION_ID = SF.ID) WHERE SF.IS_ENABLED = '1' AND SF.SUPER_FUN = 0 AND SUR.USERNAME = ? ORDER BY FDESC";
		List<JSONObject>list= ur.queryAllCustom(sql, userName);
		if(list!=null){
			 ArrayList<FunctionBean> functionBeans = JSON.parseObject(list.toString(), new TypeReference<ArrayList<FunctionBean>>() {});
			 return functionBeans;
		}
		return null;
	}
	
   private static String decryptAES(String data, String pass) throws Exception {
	        Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
	        SecretKeySpec keyspec = new SecretKeySpec(pass.getBytes(), KEY_ALGORITHM);
	        IvParameterSpec ivspec = new IvParameterSpec(pass.getBytes());
	        cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);
	        byte[] result = cipher.doFinal(Base64.decode(data.getBytes(CharsetUtil.UTF_8)));
	        return new String(result, CharsetUtil.UTF_8);
  }

}
