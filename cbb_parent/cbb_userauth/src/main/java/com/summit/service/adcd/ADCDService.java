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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.summit.common.entity.ADCDBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.domain.adcd.ADCDBeanRowMapper;
import com.summit.repository.UserRepository;
import com.summit.util.Page;

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
	private ADCDBeanRowMapper atm;
	/**
	 * 
	 * 查询adcd树
	 * @return
	 * @throws Exception 
	 */
	public ADCDBean queryAdcdTree(String padcd) throws Exception {
		LinkedMap linkedMap=new LinkedMap();
		StringBuffer sql = new StringBuffer("SELECT ADCD, ADNM,PADCD, ADLEVEL FROM AD_CD_B where 1=1 ");
		if(padcd==null || "".equals(padcd)){
			sql.append(" and (padcd is null  or padcd='-1' )");
		}else{
			sql.append(" and padcd =? ");
			linkedMap.put(1, padcd);
			
		}
		List<Object> rootList= ur.queryAllCustom(sql.toString(),linkedMap);
        ADCDBean ADCDBeanTree =null;
        if(rootList.size()>0){
        	String jsonTree=((JSONObject)rootList.get(0)).toString();
			ADCDBeanTree = JSON.parseObject(jsonTree, new TypeReference<ADCDBean>() {});
			List<ADCDBean> list=generateOrgMapToTree(null,ADCDBeanTree.getAdcd());
			if(list!=null && list.size()>0){
				ADCDBeanTree.setChildren(list);
			}
        	  
		}
		return ADCDBeanTree;
	}
	
   public List<ADCDBean> generateOrgMapToTree(Map<String, List<Object>>  orgMaps, String pid)  {
        if (null == orgMaps || orgMaps.size() == 0) {//a.ADLEVEL as LEVELa ,b.ADLEVEL as LEVELb
        	StringBuffer querySql = new StringBuffer("SELECT a.ADCD, a.ADNM,a.PADCD, b.ADCD AS CHILD_ID, b.ADNM AS CHILD_NAME FROM AD_CD_B AS a  ");
        	querySql.append(" JOIN AD_CD_B AS b ON b.PADCD = a.ADCD ORDER BY  a.ADCD ASC,b.ADCD asc");
        	JSONArray list=null;
			try {
				list = ur.queryAllCustomJsonArray(querySql.toString(),null);
			} catch (Exception e) {
				e.printStackTrace();
			}
    		Map<String, List<Object>> map=new HashMap<String, List<Object>>();
    		List<Object> childrenList=new ArrayList<Object>();;
    		String adcd="";
    		int i=0;
    		for(Object o:list){
    			JSONObject jSONObject=(JSONObject)o;
    			if(!"".equals(adcd) && !adcd.equals(jSONObject.getString("ADCD"))){
    				map.put(adcd, childrenList);
    				childrenList=new ArrayList<Object>();
    			}
    			childrenList.add(jSONObject);
    			adcd=jSONObject.getString("ADCD");
    			if(i==list.size()-1){
    				map.put(adcd, childrenList);
    			}
    			i++;
    			
    		}
    		orgMaps=map;
        }
        logger.debug("2:"+orgMaps.size());
        List<ADCDBean> orgList = new ArrayList<>();
        if (orgMaps != null && orgMaps.size() > 0) {
        	List parenList=orgMaps.get(pid);
        	if(parenList==null){
        		return orgList;
        	}
        	logger.debug("3:"+parenList.size());
            for (Object obj : parenList) {
            	// logger.debug("3-1:"+i);
            	ADCDBean adcdBean=new ADCDBean();
            	// JSONObject jSONOTree=new JSONObject();
            	JSONObject json=(JSONObject)obj;
            	System.out.println(json);
            	adcdBean.setAdcd(json.getString("CHILD_ID"));
            	adcdBean.setAdnm(json.getString("CHILD_NAME"));
            	adcdBean.setPadcd(pid);
            	if(json.containsKey("LEVELb")){
            		adcdBean.setLevel(json.getString("LEVELb"));
             	}
            	 List<ADCDBean> children = generateOrgMapToTree(orgMaps, json.get("CHILD_ID").toString());
                //将子结果集存入当前对象的children字段中
                if(children!=null && children.size()>0){
                	adcdBean.setChildren(children);
                }
                //添加当前对象到主结果集中
                orgList.add(adcdBean);
            }
        }
        return orgList;
    }


	/**
	 * 根据adcd查询
	 * @param adcd
	 * @return
	 */
	public List<ADCDBean> queryByPId(JSONObject paramJson)throws Exception{
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
		List<Object> l=ur.queryAllCustom(sql.toString(), map);
		if(l!=null){
			 ArrayList<ADCDBean> adCDBeans = JSON.parseObject(l.toString(), new TypeReference<ArrayList<ADCDBean>>() {});
			 return adCDBeans;
				
		}else{
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
	public Page<ADCDBean> queryByPage(int start, int limit, String padcd) {
		String sql = "SELECT * FROM AD_CD_B WHERE PADCD = ?";
		//if("".equals(padcd))padcd ="root";
		Page<JSONObject> rs = ur.queryByCustomPage(sql, start, limit, padcd);
		
		Page<ADCDBean> pageADCDBeanInfo=new Page<ADCDBean>();
		if(rs!=null){
			 ArrayList<ADCDBean> students = JSON.parseObject(rs.getContent().toString(), new TypeReference<ArrayList<ADCDBean>>() {});
			 pageADCDBeanInfo.setContent(students);
			 pageADCDBeanInfo.setTotalElements(rs.getTotalElements());
			 return pageADCDBeanInfo;
		}
		return null;
	}


	/**
	 * 
	 * 编辑（保存）
	 * @param 
	 * @return
	 */
	public void edit(ADCDBean ab) {
		String sql = "UPDATE AD_CD_B SET  ADNM = ?, PADCD = ?, ADLEVEL = ? where ADCD = ?";
		jdbcTemplate.update(
				sql,
				ab.getAdnm(),
				ab.getPadcd(),
				ab.getLevel(),
				ab.getAdcd()
		);
	}
	/**
	 * 新增
	 */
	public ResponseCodeEnum add(ADCDBean ab) {
		String hasadcd="select * from AD_CD_B where adcd='"+ab.getAdcd()+"'";
		List l=ur.queryAllCustom(hasadcd);
		if(l.size()>0){
			return ResponseCodeEnum.CODE_9992;
		}
		String sql = "INSERT INTO AD_CD_B (ADCD, ADNM, PADCD,ADLEVEL) VALUES (?, ? ,?, ?)";
		jdbcTemplate.update(
				sql,
				ab.getAdcd(),
				ab.getAdnm(),
				ab.getPadcd(),
				ab.getLevel());
		return null;
	}


	/**
	 * 
	 * 
	 * 删除
	 * @param ids
	 * @return
	 */
	public void del(String ids) {
		ids = ids.replaceAll(",", "','");
		//String sql = "SELECT * FROM AD_CD_B WHERE PADCD IN ('" + ids + "')";
		//List<ADCDBean> l = ur.queryAllCustom(sql, atm);
		//if (st.collectionNotNull(l)) {
		//	return ResponseCodeBySummit.CODE_9981;
		//}
		String sql = "DELETE FROM AD_CD_B WHERE ADCD IN ('" + ids+ "') ";
		jdbcTemplate.update(sql);
		//return ResponseCodeBySummit.CODE_0000;
	}




}
