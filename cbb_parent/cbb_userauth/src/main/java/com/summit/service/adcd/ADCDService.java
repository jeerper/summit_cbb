package com.summit.service.adcd;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.summit.domain.adcd.ADCDBean;
import com.summit.domain.adcd.ADCDBeanRowMapper;
import com.summit.domain.adcd.ADCDTreeBean;
import com.summit.domain.adcd.ADCDTreeBeanRowMapper;
import com.summit.repository.UserRepository;
import com.summit.util.Page;
import com.summit.util.SummitTools;

@Service
@Transactional
public class ADCDService {
	@Autowired
	private UserRepository ur;
	@Autowired
	public JdbcTemplate jdbcTemplate;
	@Autowired
	private SummitTools st;
	@Autowired
	private ADCDTreeBeanRowMapper atrm;
	@Autowired
	private ADCDBeanRowMapper atm;
	/**
	 * 
	 * 查询adcd树
	 * @return
	 */
	public List<ADCDTreeBean> queryAdcdTree() {
		String sql = "SELECT ADCD,PADCD,ADNM,LEVEL  FROM AD_CD_B";
		List<ADCDTreeBean> all = ur.queryAllCustom(sql, atrm);
		List<ADCDTreeBean> list = new ArrayList<ADCDTreeBean>();
		for (ADCDTreeBean adcdTreeBean : all) {
			if(adcdTreeBean.getPadcd() == null){
				adcdTreeBean.setOpen(true);
			}
			list.add(adcdTreeBean);
		}
		return list;
	}
	/**
	 * 根据adcd查询
	 * @param adcd
	 * @return
	 */
	public Map<String, Object> queryById(String adcd) {
		String sql = "SELECT * FROM AD_CD_B WHERE ADCD = ?";
		List<ADCDBean> l = ur.queryAllCustom(sql, atm, adcd);
		if (st.collectionIsNull(l)) {
			return st.error("");
		}
		return st.success("", l.get(0));
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
	public Map<String, Object> edit(ADCDBean ab) {
		String sql = "UPDATE AD_CD_B SET  ADNM = ?, PADCD = ?, LEVEL = ? where ADCD = ?";
		jdbcTemplate.update(
				sql,
				ab.getAdnm(),
				ab.getPadcd(),
				ab.getLevel(),
				ab.getAdcd()
		);
		return st.success("");
	}
	/**
	 * 新增
	 */
	public Map<String, Object> add(ADCDBean ab) {
		String hasadcd="select * from AD_CD_B where adcd='"+ab.getAdcd()+"'";
		List l=ur.queryAllCustom(hasadcd);
		if(l.size()>0){
			return st.error(",重复的行政区划编码");
		}
		String sql = "INSERT INTO AD_CD_B (ADCD, ADNM, PADCD,LEVEL) VALUES (?, ? ,?, ?)";
		jdbcTemplate.update(
				sql,
				ab.getAdcd(),
				ab.getAdnm(),
				ab.getPadcd(),
				ab.getLevel());
		return st.success("");
	}


	/**
	 * 
	 * 
	 * 删除
	 * @param ids
	 * @return
	 */
	public Map<String, Object> del(String ids) {
		ids = ids.replaceAll(",", "','");
		String sql = "SELECT * FROM AD_CD_B WHERE PADCD IN ('" + ids + "')";
		List<ADCDBean> l = ur.queryAllCustom(sql, atm);
		if (st.collectionNotNull(l)) {
			return st.error("不能删除包含子节点的数据");
		}
		sql = "DELETE FROM AD_CD_B WHERE ADCD IN ('" + ids+ "') ";
		jdbcTemplate.update(sql);
		return st.success("");
	}




}
