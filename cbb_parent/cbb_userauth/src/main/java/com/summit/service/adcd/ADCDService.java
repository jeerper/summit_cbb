package com.summit.service.adcd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.summit.common.entity.ResponseCodeBySummit;
import com.summit.controller.UserController;
import com.summit.domain.adcd.ADCDBean;
import com.summit.domain.adcd.ADCDBeanRowMapper;
import com.summit.repository.UserRepository;
import com.summit.util.Page;
import com.summit.util.SummitTools;

import net.sf.json.JSONObject;

@Service
@Transactional
public class ADCDService {
	private static final Logger logger = LoggerFactory.getLogger(ADCDService.class);
	@Autowired
	private UserRepository ur;
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	private SummitTools st;
	@Autowired
	private ADCDBeanRowMapper atm;
	/**
	 * 
	 * 查询adcd树
	 * @return
	 */
	public JSONObject queryAdcdTree(String padcd) {
		JSONObject jSONOTree=null;
		LinkedMap linkedMap=new LinkedMap();
		StringBuffer sql = new StringBuffer("SELECT ADCD, ADNM,PADCD, ADLEVEL FROM AD_CD_B where 1=1 ");
		if(padcd==null || "".equals(padcd)){
			sql.append(" and (padcd is null  or padcd='-1' )");
		}else{
			sql.append(" and padcd =? ");
			linkedMap.put(1, padcd);
			
		}
		
        try {
			List<Object> rootList= ur.queryAllCustom(sql.toString(),linkedMap);
			if(rootList.size()>0){
				jSONOTree=(JSONObject)rootList.get(0);
				logger.debug("jSONOTree.getString: "+jSONOTree.getString("ADCD"));
				List<JSONObject> list=null;
				list=generateOrgMapToTree(null,jSONOTree.getString("ADCD"));
				logger.debug("list: "+list.size());
	        	jSONOTree.put("children", list);
			}
			
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return jSONOTree;
	}
	
   public List<JSONObject> generateOrgMapToTree(Map<String, List<Object>>  orgMaps, String pid) throws Exception {
        if (null == orgMaps || orgMaps.size() == 0) {
        	StringBuffer sql = new StringBuffer("SELECT a.ADCD, a.ADNM,a.PADCD, b.ADCD AS child_id, b.ADNM AS child_name,a.ADLEVEL as LEVELa ,b.ADLEVEL as LEVELb FROM AD_CD_B AS a  ");
        			sql.append(" JOIN AD_CD_B AS b ON b.PADCD = a.ADCD ORDER BY  a.ADCD ASC,b.ADCD asc");
        	logger.debug(sql.toString());
        	List<Object> list= ur.queryAllCustom(sql.toString(),new LinkedMap());
    		Map<String, List<Object>> map=new HashMap<String, List<Object>>();
    		List<Object> childrenList=new ArrayList();;
    		String adcd="";
    		int i=0;
    		for(Object s:list){
    			i++;
    			JSONObject JSONObject=(JSONObject)s;
    			if(!"".equals(adcd) && !adcd.equals(JSONObject.getString("ADCD")) || i==list.size()-1){
    				map.put(adcd, childrenList);
    				childrenList=new ArrayList();
    			}
    			childrenList.add(JSONObject);
    			adcd=JSONObject.getString("ADCD");
    		}
    		orgMaps=map;
//            String json_list = JSONObject.toJSONString(list);
//            orgMaps = (List<Map<String, Object>>) JSONObject.parse(json_list);
        }
        List<JSONObject> orgList = new ArrayList<>();
        if (orgMaps != null && orgMaps.size() > 0) {
        	List parenList=orgMaps.get(pid);
        	if(parenList==null){
        		return orgList;
        	}
            for (Object obj : parenList) {
            	JSONObject jSONOTree=new JSONObject();
            	JSONObject json=(JSONObject)obj;
            	jSONOTree.put("adcd", json.getString("child_id"));
            	jSONOTree.put("adnm", json.getString("child_name"));
            	jSONOTree.put("padcd",pid);
            	if(json.containsKey("LEVELb")){
            	   jSONOTree.put("adlevel",json.getString("LEVELb"));
            	}
                List<JSONObject> children = generateOrgMapToTree(orgMaps, json.get("child_id").toString());
                //将子结果集存入当前对象的children字段中
                jSONOTree.put("children", children);
                //添加当前对象到主结果集中
                orgList.add(jSONOTree);
                
            }
        }
        return orgList;
    }


	/**
	 * 根据adcd查询
	 * @param adcd
	 * @return
	 */
	public List<Object> queryByPId(JSONObject paramJson) {
		StringBuffer sql =new StringBuffer( "SELECT * FROM AD_CD_B WHERE 1=1 ");
		LinkedMap map = new LinkedMap();
		if(paramJson!=null){
            Integer index = 1;
			if(paramJson.containsKey("padcd")){
				sql.append(" and  padcd = ?");
				map.put(index, paramJson.get("padcd") );
                index++;
			}
			if(paramJson.containsKey("level")){
				sql.append(" and  ADLEVEL = ?");
				map.put(index, paramJson.get("level") );
                index++;
			}
		}
		List<Object> l;
		try {
			l = ur.queryAllCustom(sql.toString(), map);
			return l;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	/**
	 * 根据adcds查询
	 * @param adcds
	 * @return
	 */
	public List<ADCDBean> queryByAdcds(String adcds) {
		adcds = adcds.replaceAll(",", "','");
		String sql = "SELECT * FROM AD_CD_B WHERE ADCD IN ('"+ adcds + "') ";
		List<ADCDBean> l = ur.queryAllCustom(sql, atm, null);
		return l;
	}
	/**
	 * 
	 * 编辑（查询）
	 * @param start
	 * @param limit
	 * @param pId
	 * @return
	 */
	public Page<JSONObject> queryByPage(int start, int limit, String padcd) {
		String sql = "SELECT * FROM AD_CD_B WHERE PADCD = ?";
		//if("".equals(padcd))padcd ="root";
		Page<JSONObject> rs = ur.queryByCustomPage(sql, start, limit, padcd);
		return rs;
	}


	/**
	 * 
	 * 编辑（保存）
	 * @param 
	 * @return
	 */
	public ResponseCodeBySummit edit(ADCDBean ab) {
		String sql = "UPDATE AD_CD_B SET  ADNM = ?, PADCD = ?, ADLEVEL = ? where ADCD = ?";
		jdbcTemplate.update(
				sql,
				ab.getAdnm(),
				ab.getPadcd(),
				ab.getLevel(),
				ab.getAdcd()
		);
		return ResponseCodeBySummit.CODE_0000;
	}
	/**
	 * 新增
	 */
	public ResponseCodeBySummit add(ADCDBean ab) {
		String hasadcd="select * from AD_CD_B where adcd='"+ab.getAdcd()+"'";
		List l=ur.queryAllCustom(hasadcd);
		if(l.size()>0){
			return ResponseCodeBySummit.CODE_4033;
		}
		String sql = "INSERT INTO AD_CD_B (ADCD, ADNM, PADCD,ADLEVEL) VALUES (?, ? ,?, ?)";
		jdbcTemplate.update(
				sql,
				ab.getAdcd(),
				ab.getAdnm(),
				ab.getPadcd(),
				ab.getLevel());
		return ResponseCodeBySummit.CODE_0000;
	}


	/**
	 * 
	 * 
	 * 删除
	 * @param ids
	 * @return
	 */
	public ResponseCodeBySummit del(String ids) {
		ids = ids.replaceAll(",", "','");
		String sql = "SELECT * FROM AD_CD_B WHERE PADCD IN ('" + ids + "')";
		List<ADCDBean> l = ur.queryAllCustom(sql, atm);
		if (st.collectionNotNull(l)) {
			return ResponseCodeBySummit.CODE_9981;
		}
		sql = "DELETE FROM AD_CD_B WHERE ADCD IN ('" + ids+ "') ";
		jdbcTemplate.update(sql);
		return ResponseCodeBySummit.CODE_0000;
	}




}
