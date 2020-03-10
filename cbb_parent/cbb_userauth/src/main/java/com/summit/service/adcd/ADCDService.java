package com.summit.service.adcd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.summit.common.entity.UserInfo;
import org.apache.commons.collections.map.LinkedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;
import com.summit.cbb.utils.page.Page;
import com.summit.common.entity.ADCDBean;
import com.summit.common.entity.ADCDTreeBean;
import com.summit.common.entity.ResponseCodeEnum;
import com.summit.repository.UserRepository;

import net.sf.json.JSONObject;


@Service
public class ADCDService {

    @Autowired
    private UserRepository ur;

    @Autowired
    public JdbcTemplate jdbcTemplate;

    /**
     * 查询adcd树
     *
     * @return
     * @throws Exception
     */
    public ADCDBean queryAdcdTree(String padcd, boolean isQueryAll) throws Exception {
        LinkedMap linkedMap = new LinkedMap();
        StringBuffer sql = new StringBuffer("SELECT ADCD, ADNM,PADCD, ADLEVEL FROM SYS_AD_CD where 1=1 ");
        if (padcd == null || "".equals(padcd)) {
            sql.append(" and (padcd is null  or padcd='-1' )");
        } else {
            sql.append(" and adcd =? ");
            linkedMap.put(1, padcd);

        }
        List<Object> rootList = ur.queryAllCustom(sql.toString(), linkedMap);
        ADCDBean ADCDBeanTree = null;
        if (rootList.size() > 0) {
            String jsonTree = ((JSONObject) rootList.get(0)).toString();
            ADCDBeanTree = JSON.parseObject(jsonTree, new TypeReference<ADCDBean>() {
            });
            List<ADCDBean> list = generateOrgMapToTree(null, ADCDBeanTree.getAdcd(), isQueryAll);
            if (list != null && list.size() > 0) {
                ADCDBeanTree.setChildren(list);
                ADCDBeanTree.setHasChild(true);
            }
        }
        return ADCDBeanTree;
    }

    public List<ADCDBean> generateOrgMapToTree(Map<String, List<Object>> orgMaps, String pid, boolean isQueryAll) throws Exception {
        if (null == orgMaps || orgMaps.size() == 0) {//a.ADLEVEL as LEVELa ,b.ADLEVEL as LEVELb
            StringBuffer querySql = new StringBuffer(" SELECT a.ADCD, a.ADNM,a.PADCD, b.ADCD AS CHILD_ID, b.ADNM AS CHILD_NAME ");
            LinkedMap whereMap = new LinkedMap();
            if (!isQueryAll) {
                querySql.append(" ,(select count(*) from SYS_AD_CD where padcd=b.ADCD ) countChildren ");
            }
            querySql.append(" FROM SYS_AD_CD AS a  ");
            querySql.append(" JOIN SYS_AD_CD AS b ON b.PADCD = a.ADCD ");
            if (!isQueryAll) {
                querySql.append(" where b.padcd=? ");
                whereMap.put(1, pid);
            }
            querySql.append(" ORDER BY  a.ADCD ASC,b.ADCD asc");
            JSONArray list = null;
            list = ur.queryAllCustomJsonArray(querySql.toString(), whereMap);

            Map<String, List<Object>> map = new HashMap<String, List<Object>>();
            List<Object> childrenList = new ArrayList<Object>();
            ;
            String adcd = "";
            int i = 0;
            for (Object o : list) {
                JSONObject jSONObject = (JSONObject) o;
                if (!"".equals(adcd) && !adcd.equals(jSONObject.getString("ADCD"))) {
                    map.put(adcd, childrenList);
                    childrenList = new ArrayList<Object>();
                }
                childrenList.add(jSONObject);
                adcd = jSONObject.getString("ADCD");
                if (i == list.size() - 1) {
                    map.put(adcd, childrenList);
                }
                i++;

            }
            orgMaps = map;
        }
        List<ADCDBean> orgList = new ArrayList<>();
        if (orgMaps != null && orgMaps.size() > 0) {
            List parenList = orgMaps.get(pid);
            if (parenList == null) {
                return orgList;
            }
            for (Object obj : parenList) {
                // logger.debug("3-1:"+i);
                ADCDBean adcdBean = new ADCDBean();
                // JSONObject jSONOTree=new JSONObject();
                JSONObject json = (JSONObject) obj;
                //System.out.println(json);
                adcdBean.setAdcd(json.getString("CHILD_ID"));
                adcdBean.setAdnm(json.getString("CHILD_NAME"));
                adcdBean.setPadcd(pid);
                if (json.containsKey("LEVELb")) {
                    adcdBean.setLevel(json.getString("LEVELb"));
                }
                if (json.containsKey("countChildren")) {
                    if (json.getString("countChildren") != null && json.getInt("countChildren") > 0) {
                        adcdBean.setHasChild(true);
                    } else {
                        adcdBean.setHasChild(false);
                    }
                }
                //if(isQueryAll){
                List<ADCDBean> children = generateOrgMapToTree(orgMaps, json.get("CHILD_ID").toString(), isQueryAll);
                //将子结果集存入当前对象的children字段中
                if (children != null && children.size() > 0) {
                    adcdBean.setChildren(children);
                }
                //}
                //添加当前对象到主结果集中
                orgList.add(adcdBean);
            }
        }
        return orgList;
    }
    public List<ADCDBean> generateOrgMapToTree(Map<String, List<Object>> orgMaps, String adlevel,String pid, boolean isQueryAll) throws Exception {
        if (null == orgMaps || orgMaps.size() == 0) {//a.ADLEVEL as LEVELa ,b.ADLEVEL as LEVELb
            StringBuffer querySql = new StringBuffer(" SELECT a.ADCD, a.ADNM,a.PADCD, b.ADCD AS CHILD_ID, b.ADNM AS CHILD_NAME ");
            LinkedMap whereMap = new LinkedMap();
            if (!isQueryAll) {
                querySql.append(" ,(select count(*) from SYS_AD_CD where padcd=b.ADCD ) countChildren ");
            }
            querySql.append(" FROM SYS_AD_CD AS a  ");
            querySql.append(" JOIN SYS_AD_CD AS b ON b.PADCD = a.ADCD where 1=1");
            if (!isQueryAll) {
                querySql.append(" and b.padcd=? ");
                whereMap.put(1, pid);
            }
            if (adlevel == null || "".equals(adlevel)) {
            	
            }else {
            	querySql.append(" and b.ADLEVEL <=? ");
                if(whereMap.size()>0)
                	whereMap.put(2, adlevel);
                 else 
                	whereMap.put(1, adlevel);      	
            }
            querySql.append(" ORDER BY  a.ADCD ASC,b.ADCD asc");
            JSONArray list = null;
            list = ur.queryAllCustomJsonArray(querySql.toString(), whereMap);

            Map<String, List<Object>> map = new HashMap<String, List<Object>>();
            List<Object> childrenList = new ArrayList<Object>();
            ;
            String adcd = "";
            int i = 0;
            for (Object o : list) {
                JSONObject jSONObject = (JSONObject) o;
                if (!"".equals(adcd) && !adcd.equals(jSONObject.getString("ADCD"))) {
                    map.put(adcd, childrenList);
                    childrenList = new ArrayList<Object>();
                }
                childrenList.add(jSONObject);
                adcd = jSONObject.getString("ADCD");
                if (i == list.size() - 1) {
                    map.put(adcd, childrenList);
                }
                i++;

            }
            orgMaps = map;
        }
        List<ADCDBean> orgList = new ArrayList<>();
        if (orgMaps != null && orgMaps.size() > 0) {
            List parenList = orgMaps.get(pid);
            if (parenList == null) {
                return orgList;
            }
            for (Object obj : parenList) {
                // logger.debug("3-1:"+i);
                ADCDBean adcdBean = new ADCDBean();
                // JSONObject jSONOTree=new JSONObject();
                JSONObject json = (JSONObject) obj;
                //System.out.println(json);
                adcdBean.setAdcd(json.getString("CHILD_ID"));
                adcdBean.setAdnm(json.getString("CHILD_NAME"));
                adcdBean.setPadcd(pid);
                if (json.containsKey("LEVELb")) {
                    adcdBean.setLevel(json.getString("LEVELb"));
                }
                if (json.containsKey("countChildren")) {
                    if (json.getString("countChildren") != null && json.getInt("countChildren") > 0) {
                        adcdBean.setHasChild(true);
                    } else {
                        adcdBean.setHasChild(false);
                    }
                }
                //if(isQueryAll){
                List<ADCDBean> children = generateOrgMapToTree(orgMaps,adlevel, json.get("CHILD_ID").toString(), isQueryAll);
                //将子结果集存入当前对象的children字段中
                if (children != null && children.size() > 0) {
                    adcdBean.setChildren(children);
                }
                //}
                //添加当前对象到主结果集中
                orgList.add(adcdBean);
            }
        }
        return orgList;
    }

    public List<ADCDTreeBean> queryJsonAdcdTreeList(String padcd, boolean isQueryAll) throws Exception {
        LinkedMap linkedMap = new LinkedMap();
        StringBuffer sql = new StringBuffer("SELECT ADCD as value, ADNM as title,PADCD, ADLEVEL FROM SYS_AD_CD where 1=1 ");
        if (padcd == null || "".equals(padcd)) {
            sql.append(" and (padcd is null  or padcd='-1' )");
        } else {
            sql.append(" and adcd =? ");
            linkedMap.put(1, padcd);

        }
        List<Object> rootList = ur.queryAllCustom(sql.toString(), linkedMap);
        if (rootList.size() > 0) {
            List<ADCDTreeBean> aDCDTreeBeanList = new ArrayList<ADCDTreeBean>();
            ADCDTreeBean adcdTreeBean = null;
            for (int i = 0; i < rootList.size(); i++) {
                String jsonTree = ((JSONObject) rootList.get(i)).toString();
                adcdTreeBean = JSON.parseObject(jsonTree, new TypeReference<ADCDTreeBean>() {
                });
                List<ADCDBean> list = generateOrgMapToTree(null, adcdTreeBean.getValue(), isQueryAll);
                if (list != null && list.size() > 0) {
                    List<ADCDTreeBean> adcdTreeBeanList = new ArrayList<ADCDTreeBean>();
                    ADCDTreeBean adcdTreeBean1 = null;
                    for (ADCDBean adcdBean : list) {
                        adcdTreeBean1 = new ADCDTreeBean();
                        adcdTreeBean1.setValue(adcdBean.getAdcd());
                        adcdTreeBean1.setTitle(adcdBean.getAdnm());
                        adcdTreeBean1.setLevel(adcdBean.getLevel());
                        adcdTreeBean1.setPadcd(adcdBean.getPadcd());
                        if (isQueryAll) {
                            adcdTreeBean1.setChildren(getAdcdTreeBean(adcdBean.getChildren()));
                        }
                        adcdTreeBeanList.add(adcdTreeBean1);
                    }
                    adcdTreeBean.setChildren(adcdTreeBeanList);

                }
                aDCDTreeBeanList.add(adcdTreeBean);
            }

            return aDCDTreeBeanList;
        }
        return null;
    }

    public ADCDTreeBean queryJsonAdcdTreeByAdlevel(String adlevel,String padcd, boolean isQueryAll) throws Exception {
        LinkedMap linkedMap = new LinkedMap();
        StringBuffer sql = new StringBuffer("SELECT ADCD as value, ADNM as title,PADCD, ADLEVEL FROM SYS_AD_CD where 1=1 ");
        if (padcd == null || "".equals(padcd)) {
            sql.append(" and (padcd is null  or padcd='-1' )");
        } else {
            sql.append(" and adcd =? ");
            linkedMap.put(1, padcd);

        }
        if (adlevel == null || "".equals(adlevel)) {
        	
        }else {
            sql.append(" and ADLEVEL <=? ");
            if(linkedMap.size()>0)
               linkedMap.put(2, adlevel);
            else 
               linkedMap.put(1, adlevel);
        }
        List<Object> rootList = ur.queryAllCustom(sql.toString(), linkedMap);
        ADCDTreeBean adcdTreeBean = null;
        if (rootList.size() > 0) {
            for (int i = 0; i < rootList.size(); i++) {
                String jsonTree = ((JSONObject) rootList.get(i)).toString();
                adcdTreeBean = JSON.parseObject(jsonTree, new TypeReference<ADCDTreeBean>() {
                });
                List<ADCDBean> list = generateOrgMapToTree(null, adlevel,adcdTreeBean.getValue(), isQueryAll);
                if (list != null && list.size() > 0) {
                    List<ADCDTreeBean> adcdTreeBeanList = new ArrayList<ADCDTreeBean>();
                    ADCDTreeBean adcdTreeBean1 = null;
                    for (ADCDBean adcdBean : list) {
                        adcdTreeBean1 = new ADCDTreeBean();
                        adcdTreeBean1.setValue(adcdBean.getAdcd());
                        adcdTreeBean1.setTitle(adcdBean.getAdnm());
                        adcdTreeBean1.setLevel(adcdBean.getLevel());
                        adcdTreeBean1.setPadcd(adcdBean.getPadcd());
                        if (isQueryAll) {
                            adcdTreeBean1.setChildren(getAdcdTreeBean(adcdBean.getChildren()));
                        }
                        adcdTreeBeanList.add(adcdTreeBean1);
                    }
                    adcdTreeBean.setChildren(adcdTreeBeanList);

                }
            }


        }
        return adcdTreeBean;
    }
    
    public ADCDTreeBean queryJsonAdcdTree(String padcd, boolean isQueryAll) throws Exception {
        LinkedMap linkedMap = new LinkedMap();
        StringBuffer sql = new StringBuffer("SELECT ADCD as value, ADNM as title,PADCD, ADLEVEL FROM SYS_AD_CD where 1=1 ");
        if (padcd == null || "".equals(padcd)) {
            sql.append(" and (padcd is null  or padcd='-1' )");
        } else {
            sql.append(" and adcd =? ");
            linkedMap.put(1, padcd);

        }
        List<Object> rootList = ur.queryAllCustom(sql.toString(), linkedMap);
        ADCDTreeBean adcdTreeBean = null;
        if (rootList.size() > 0) {
            for (int i = 0; i < rootList.size(); i++) {
                String jsonTree = ((JSONObject) rootList.get(i)).toString();
                adcdTreeBean = JSON.parseObject(jsonTree, new TypeReference<ADCDTreeBean>() {
                });
                List<ADCDBean> list = generateOrgMapToTree(null, adcdTreeBean.getValue(), isQueryAll);
                if (list != null && list.size() > 0) {
                    List<ADCDTreeBean> adcdTreeBeanList = new ArrayList<ADCDTreeBean>();
                    ADCDTreeBean adcdTreeBean1 = null;
                    for (ADCDBean adcdBean : list) {
                        adcdTreeBean1 = new ADCDTreeBean();
                        adcdTreeBean1.setValue(adcdBean.getAdcd());
                        adcdTreeBean1.setTitle(adcdBean.getAdnm());
                        adcdTreeBean1.setLevel(adcdBean.getLevel());
                        adcdTreeBean1.setPadcd(adcdBean.getPadcd());
                        if (isQueryAll) {
                            adcdTreeBean1.setChildren(getAdcdTreeBean(adcdBean.getChildren()));
                        }
                        adcdTreeBeanList.add(adcdTreeBean1);
                    }
                    adcdTreeBean.setChildren(adcdTreeBeanList);

                }
            }


        }
        return adcdTreeBean;
    }

    public List<String> getAdcdBean(String pid) throws Exception {
        StringBuffer querySql = new StringBuffer(" select adcd from ( select t1.adcd, ");
        querySql.append(" if(find_in_set(padcd, @pids) > 0, @pids := concat(@pids, ',', adcd), 0) as ischild ");
        querySql.append("  from ( select adcd,padcd from sys_ad_cd t order by padcd, adcd) t1,(select @pids := ? ) t2 ");
        querySql.append(" ) t3 where ischild != 0 ");
        LinkedMap linkedMap = new LinkedMap();
        linkedMap.put(1, pid);
        List<Object> l = ur.queryAllCustom(querySql.toString(), linkedMap);
        List<String> adcdList = new ArrayList<String>();
        if (l.size() > 0) {
            // adcdList.add(pid);
            for (Object adcd : l) {
                adcdList.add(((JSONObject) adcd).getString("adcd"));
            }
            return adcdList;
        }
        return null;
    }

    private List<ADCDTreeBean> getAdcdTreeBean(List<ADCDBean> children) {
        if (children != null && children.size() > 0) {
            List<ADCDTreeBean> adcdTreeBeanList = new ArrayList<ADCDTreeBean>();
            ADCDTreeBean adcdTreeBean1 = null;
            for (ADCDBean adcdBean : children) {
                adcdTreeBean1 = new ADCDTreeBean();
                adcdTreeBean1.setValue(adcdBean.getAdcd());
                adcdTreeBean1.setTitle(adcdBean.getAdnm());
                adcdTreeBean1.setLevel(adcdBean.getLevel());
                adcdTreeBean1.setPadcd(adcdBean.getPadcd());
                List<ADCDTreeBean> adcdChildren = getAdcdTreeBean(adcdBean.getChildren());
                if (adcdChildren != null && adcdChildren.size() > 0) {
                    adcdTreeBean1.setChildren(adcdChildren);
                }
                adcdTreeBeanList.add(adcdTreeBean1);
            }
            return adcdTreeBeanList;
        }
        return null;
    }

    public Map<String, ADCDBean> queryAdcdMap(String adcds, String padcd) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT * FROM SYS_AD_CD WHERE 1=1 ");
        LinkedMap map = new LinkedMap();
        Integer index = 1;
        if (adcds != null && !"".equals(adcds)) {
            adcds = adcds.replaceAll(",", "','");
            sql.append(" and  ADCD in (?) ");
            map.put(index, adcds);
            index++;
        }
        if (padcd != null && !"".equals(padcd)) {
            sql.append(" and  padcd = ?");
            map.put(index, padcd);
            index++;
        }
        List<Object> l = ur.queryAllCustom(sql.toString(), map);
        if (l != null) {
            Map<String, ADCDBean> adcdMap = new HashMap<String, ADCDBean>();
            ArrayList<ADCDBean> adCDBeans = JSON.parseObject(l.toString(), new TypeReference<ArrayList<ADCDBean>>() {
            });
            if (adCDBeans != null && adCDBeans.size() > 0) {
                for (ADCDBean adcdbean : adCDBeans) {
                    adcdMap.put(adcdbean.getAdcd(), adcdbean);
                }
            }
            return adcdMap;
        } else {
            return null;
        }

    }

    /**
     * 根据adcd查询
     *
     * @param adcd
     * @return
     */
    public List<ADCDBean> queryByPId(JSONObject paramJson) throws Exception {
        StringBuffer sql = new StringBuffer("SELECT * FROM SYS_AD_CD WHERE 1=1 ");
        LinkedMap map = new LinkedMap();
        if (paramJson != null) {
            Integer index = 1;
            if (paramJson.containsKey("padcd")) {
                sql.append(" and  padcd = ?");
                map.put(index, paramJson.get("padcd"));
                index++;
            }
            if (paramJson.containsKey("level")) {
                sql.append(" and  ADLEVEL = ?");
                map.put(index, paramJson.get("level"));
                index++;
            }
        }
        List<Object> l = ur.queryAllCustom(sql.toString(), map);
        if (l != null) {
            ArrayList<ADCDBean> adCDBeans = JSON.parseObject(l.toString(), new TypeReference<ArrayList<ADCDBean>>() {
            });
            return adCDBeans;

        } else {
            return null;
        }

    }

    /**
     * 根据adcds查询
     *
     * @param adcds
     * @return
     * @throws Exception
     */
    public List<ADCDBean> queryByAdcds(String adcds) throws Exception {
        adcds = adcds.replaceAll(",", "','");
        String sql = "SELECT * FROM SYS_AD_CD WHERE ADCD IN ('" + adcds + "') ";
        LinkedMap linkedMap = new LinkedMap();
        //linkedMap.put(1,"'"+adcds+"'");
        List dataList = ur.queryAllCustom(sql, linkedMap);
        if (dataList != null && dataList.size() > 0) {
            List<ADCDBean> adcdBeanList = JSON.parseObject(dataList.toString(), new TypeReference<List<ADCDBean>>() {
            });
            return adcdBeanList;
        }
        return null;
    }
    /**
     * 根据行政区划编码查询当前行政区划下的所有人员
     * @param adcd
     * @return
     */

    public List<UserInfo> queryCurrentUserByAdcd(String adcd) throws Exception {
        StringBuilder sqlBuffer = new StringBuilder("SELECT us.NAME,us.USERNAME  from sys_user_adcd acd,sys_user us ");
        sqlBuffer.append("where acd.USERNAME=us.USERNAME and acd .ADCD = ?");
        LinkedMap lm = new LinkedMap();
        lm.put(1, adcd);
        List dataList = ur.queryAllCustom(sqlBuffer.toString(), lm);
        if (dataList != null && dataList.size() > 0) {
            List<UserInfo> adcdBeanList = JSON.parseObject(dataList.toString(), new TypeReference<List<UserInfo>>() {
            });
            return adcdBeanList;
        }
        return null;
    }

    /**
     * 编辑（查询）
     *
     * @param start
     * @param limit
     * @param pId
     * @return
     * @throws Exception
     */
    public Page<ADCDBean> queryByPage(int start, int limit, String padcd) throws Exception {
        String sql = "SELECT adcd.ADCD,adcd.ADNM,adcd.ADLEVEL,padcd.ADCD as padcd,padcd.ADNM as padnm  FROM SYS_AD_CD adcd left join SYS_AD_CD padcd on adcd.PADCD=padcd.ADCD WHERE 1=1 ";
        LinkedMap linkedMap = null;
        if (padcd != null && padcd.length() > 0) {
            sql += " and adcd.PADCD = ? ";
            linkedMap = new LinkedMap();
            linkedMap.put(1, padcd);
        }
        Page<Object> rs = ur.queryByCustomPage(sql, start, limit, linkedMap);
        if (rs != null) {
            ArrayList<ADCDBean> adcds = JSON.parseObject(rs.getContent().toString(), new TypeReference<ArrayList<ADCDBean>>() {
            });
            // return new PageImpl(adcds,rs.getPageable(),rs.getTotalElements());
            return new Page<ADCDBean>(adcds, rs.getPageable());
        }
        return null;
    }


    /**
     * 编辑（保存）
     *
     * @param
     * @return
     */
    public void edit(ADCDBean ab) {
        String sql = "UPDATE SYS_AD_CD SET  ADNM = ?, PADCD = ?, ADLEVEL = ? where ADCD = ?";
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
        String hasadcd = "select * from SYS_AD_CD where adcd='" + ab.getAdcd() + "'";
        List l = ur.queryAllCustom(hasadcd);
        if (l.size() > 0) {
            return ResponseCodeEnum.CODE_9992;
        }
        String sql = "INSERT INTO SYS_AD_CD (ADCD, ADNM, PADCD,ADLEVEL) VALUES (?, ? ,?, ?)";
        jdbcTemplate.update(
                sql,
                ab.getAdcd(),
                ab.getAdnm(),
                ab.getPadcd(),
                ab.getLevel());
        return null;
    }


    /**
     * 删除
     *
     * @param ids
     * @return
     * @throws Exception
     */
    public ResponseCodeEnum del(String ids) throws Exception {
        ids = "'" + ids.replaceAll(",", "','") + "'";
        String sql = "select padcd, count(*) from SYS_AD_CD where padcd in (" + ids + ") group by padcd";
        LinkedMap linkedMap = new LinkedMap();
        // linkedMap.put(1,ids);
        List<Object> list = ur.queryAllCustom(sql, linkedMap);
        if (list == null || list.size() == 0) {
            String updateSql = "DELETE FROM SYS_AD_CD WHERE ADCD IN (" + ids + ") ";
            jdbcTemplate.update(updateSql);
            return null;
        } else {
            return ResponseCodeEnum.CODE_0000;
        }
    }

}
