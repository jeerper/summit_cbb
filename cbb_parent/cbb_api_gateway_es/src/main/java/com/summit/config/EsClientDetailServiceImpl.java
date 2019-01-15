package com.summit.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.ClientRegistrationService;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.summit.dao.ExcuteDao;
import com.summit.dao.QueryDao;
import com.summit.domain.Authority;
import com.summit.domain.ConditionEnum;
import com.summit.domain.SummitClient;
import com.summit.exception.EsException;
import com.summit.service.impl.BaseServiceImpl;

import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;

/**
 * 
 * @author yt
 *
 */
@Service("esClientDetailService")
public class EsClientDetailServiceImpl extends BaseServiceImpl
		implements ClientDetailsService, ClientRegistrationService {

	String index = "oauth_client_details";
	@Autowired
	ExcuteDao excuteDao;

	@Autowired
	QueryDao queryDao;

	@Override
	public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
		// TODO Auto-generated method stub
		JSONObject js = new JSONObject();
		if (null != clientDetails) {
			String clientId = clientDetails.getClientId();
			JSONArray jsList = assembleJsonArray(clientId);
			if (jsList == null || jsList.size() == 1 || jsList.size() == 0) {
				convertJson(js, clientDetails);
			} else {
				throw new ClientAlreadyExistsException("Client already exists: " + clientDetails.getClientId());
			}
		}
		try {
			excuteDao.saveOrUpdate(index, js);
		} catch (EsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private JSONObject convertJson(JSONObject js, ClientDetails clientDetails) {
		js.put("clientId", clientDetails.getClientId() == null ? JSONNull.getInstance() : clientDetails.getClientId());
		js.put("resourceIds", clientDetails.getResourceIds() == null ? JSONNull.getInstance()
				: StringUtils.collectionToCommaDelimitedString(clientDetails.getResourceIds()));
		js.put("clientSecret", clientDetails.getClientSecret() == null ? JSONNull.getInstance()
				: new BCryptPasswordEncoder().encode(clientDetails.getClientSecret()));
		js.put("scope", clientDetails.getScope() == null ? JSONNull.getInstance()
				: StringUtils.collectionToCommaDelimitedString(clientDetails.getScope()));
		js.put("authorizedGrantTypes", clientDetails.getAuthorizedGrantTypes() == null ? JSONNull.getInstance()
				: StringUtils.collectionToCommaDelimitedString(clientDetails.getAuthorizedGrantTypes()));
		js.put("registeredRedirectUri", clientDetails.getRegisteredRedirectUri() == null ? JSONNull.getInstance()
				: StringUtils.collectionToCommaDelimitedString(clientDetails.getRegisteredRedirectUri()));
		js.put("authorities", clientDetails.getAuthorities() == null ? JSONNull.getInstance()
				: StringUtils.collectionToCommaDelimitedString(clientDetails.getAuthorities()));
		js.put("accessTokenValiditySeconds", clientDetails.getAccessTokenValiditySeconds() == null
				? JSONNull.getInstance() : clientDetails.getAccessTokenValiditySeconds());
		js.put("refreshTokenValiditySeconds", clientDetails.getRefreshTokenValiditySeconds() == null
				? JSONNull.getInstance() : clientDetails.getRefreshTokenValiditySeconds());
		js.put("additionalInformation", clientDetails.getAdditionalInformation() == null ? JSONNull.getInstance()
				: clientDetails.getAdditionalInformation());
		return js;
	}

	private JSONArray assembleJsonArray(String clientId) {
		JSONArray jsonList = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(ConditionEnum.COLUMNNAME.toString(), "clientId");
		jsonObject.put(ConditionEnum.SORT.toString(), "asc");
		jsonObject.put(ConditionEnum.VALUE, clientId);
		jsonObject.put(ConditionEnum.OPERATOR.toString(), "EQ");
		jsonList.add(jsonObject);
		JSONArray jsList = queryDao.findIdByField(index, jsonList);
		return jsList;
	}

	@Override
	public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
		// TODO Auto-generated method stub
		if (null != clientDetails) {

			JSONArray jsList = assembleJsonArray(clientDetails.getClientId());
			if (jsList == null || jsList.size() == 1 || jsList.size() == 0) {
				throw new NoSuchClientException("No client found with id = " + clientDetails.getClientId());
			} else {
				// clientId 唯一 所以只会有一个 ,get(0)是count 数
				JSONObject json = jsList.getJSONObject(1);
				convertJson(json, clientDetails);
				try {
					excuteDao.saveOrUpdate(index, json);
				} catch (EsException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
		// TODO Auto-generated method stub
		/*
		 * JSONObject jsonObject = new JSONObject();
		 * jsonObject.put("clientSecret", new
		 * BCryptPasswordEncoder().encode(secret));
		 */
		JSONArray jsList = assembleJsonArray(clientId);
		if (jsList == null || jsList.size() == 1 || jsList.size() == 0) {
			throw new NoSuchClientException("No client found with id = " + clientId);
		}
		try {
			JSONObject json = jsList.getJSONObject(1);
			json.put("clientSecret", new BCryptPasswordEncoder().encode(secret));
			excuteDao.saveOrUpdate(index, json);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void removeClientDetails(String clientId) throws NoSuchClientException {
		// TODO Auto-generated method stub
		JSONArray jsonList = new JSONArray();
		JSONObject jsonObject = new JSONObject();
		jsonObject.put(ConditionEnum.COLUMNNAME.toString(), "clientId");
		jsonObject.put(ConditionEnum.SORT.toString(), "asc");
		jsonObject.put(ConditionEnum.VALUE, clientId);
		jsonObject.put(ConditionEnum.OPERATOR.toString(), "EQ");
		jsonList.add(jsonObject);
		try {
			delete(index, jsonList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public List<ClientDetails> listClientDetails() {
		// TODO Auto-generated method stub
		JSONArray jsonList = queryAll(index);
		List<ClientDetails> clientDetailList = null;
		if (jsonList == null || jsonList.size() == 1 || jsonList.size() == 0) {
			throw new NoSuchClientException("No client found ");
		} else {
			clientDetailList = new ArrayList<>();
			for (int i = 1; i < jsonList.size(); i++) {

				JSONObject js = jsonList.getJSONObject(i);
				SummitClient clientDetail = new SummitClient();
				clientDetail.setClientId(js.getString("clientId"));
				// clientDetail.setClientSecret(js.getString("clientSecret"));
				clientDetail.setResourceIds(js.getString("resourceIds") == null ? null
						: StringUtils.commaDelimitedListToSet(js.getString("resourceIds")));
				clientDetail.setScope(js.getString("scope") == null ? null
						: StringUtils.commaDelimitedListToSet(js.getString("scope")));
				clientDetail.setAuthorizedGrantTypes(js.getString("authorizedGrantTypes") == null ? null
						: StringUtils.commaDelimitedListToSet(js.getString("authorizedGrantTypes")));
				clientDetail.setRegisteredRedirectUri(js.getString("registeredRedirectUri") == null ? null
						: StringUtils.commaDelimitedListToSet(js.getString("registeredRedirectUri")));
				clientDetail.setAccessTokenValiditySeconds(js.getInt("accessTokenValiditySeconds"));
				clientDetail.setRefreshTokenValiditySeconds(js.getInt("refreshTokenValiditySeconds"));
				if (js.get("additionalInformation") == null
						|| "null".equalsIgnoreCase(js.getString("additionalInformation"))) {
					Map<String, Object> map = new HashMap<String, Object>(1);
					map.put("data", "null");
					clientDetail.setAdditionalInformation(map);
				} else {
					clientDetail.setAdditionalInformation((Map<String, Object>) js.get("additionalInformation"));
				}
				if (null == js.get("authorities")) {
				} else {
					String authorStr = js.getString("authorities");
					String[] authorArr = authorStr.split(",");

					/*
					 * Authority author = new Authority(); author =
					 * SummitTools.stringtobean(authorStr, author);
					 * author.setAuthName(authorStr);
					 */

					List<Authority> authorities = new ArrayList<Authority>();
					for (String str : authorArr) {
						Authority author = new Authority();
						author.setAuthName(str);
						authorities.add(author);
					}
					clientDetail.setAuthorities(authorities);
				}

				clientDetailList.add(clientDetail);
			}
		}
		return clientDetailList;
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {
		// TODO Auto-generated method stub
		JSONArray jsList = assembleJsonArray(clientId);
		SummitClient clientDetail = null;
		if (jsList == null || jsList.size() == 1 || jsList.size() == 0) {
			throw new NoSuchClientException("No client found ");
		} else {
			JSONObject js = jsList.getJSONObject(1);
			clientDetail = new SummitClient();
			clientDetail.setClientId(js.getString("clientId"));
			clientDetail.setClientSecret(js.getString("clientSecret"));
			clientDetail.setResourceIds(js.getString("resourceIds") == null ? null
					: StringUtils.commaDelimitedListToSet(js.getString("resourceIds")));
			clientDetail.setScope(
					js.getString("scope") == null ? null : StringUtils.commaDelimitedListToSet(js.getString("scope")));
			clientDetail.setAuthorizedGrantTypes(js.getString("authorizedGrantTypes") == null ? null
					: StringUtils.commaDelimitedListToSet(js.getString("authorizedGrantTypes")));
			clientDetail.setRegisteredRedirectUri(js.getString("registeredRedirectUri") == null ? null
					: StringUtils.commaDelimitedListToSet(js.getString("registeredRedirectUri")));
			clientDetail.setAccessTokenValiditySeconds(js.getInt("accessTokenValiditySeconds"));
			clientDetail.setRefreshTokenValiditySeconds(js.getInt("refreshTokenValiditySeconds"));
			String additionalInformation = "additionalInformation";
			String authorities = "authorities";
			if (js.get(additionalInformation) == null || js.getString(additionalInformation).equalsIgnoreCase("null")) {
				Map<String, Object> map = new HashMap<String, Object>(1);
				map.put("信息为空", "");
				clientDetail.setAdditionalInformation(map);

			} else {
				clientDetail.setAdditionalInformation((Map<String, Object>) js.get("additionalInformation"));
			}
			if (null == js.get(authorities)) {

			} else {
				String authorStr = js.getString(authorities);
				String[] authorArr = authorStr.split(",");

				/*
				 * Authority author = new Authority(); author =
				 * SummitTools.stringtobean(authorStr, author);
				 * author.setAuthName(authorStr);
				 */

				List<Authority> authoritiesList = new ArrayList<Authority>();
				for (String str : authorArr) {
					Authority author = new Authority();
					author.setAuthName(str);
					authoritiesList.add(author);
				}
				clientDetail.setAuthorities(authoritiesList);
			}

		}
		return clientDetail;
	}
}
