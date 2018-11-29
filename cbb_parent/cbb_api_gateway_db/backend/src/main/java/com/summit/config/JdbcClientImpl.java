package com.summit.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.provider.ClientAlreadyExistsException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.NoSuchClientException;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.summit.domain.Authority;
import com.summit.domain.SummitClient;

@Service("jdbcClient")
public class JdbcClientImpl extends JdbcClientDetailsService {

	private static final String CLIENT_FIELDS_FOR_UPDATE = "resource_ids, scope, "
			+ "authorized_grant_types, web_server_redirect_uri, authorities, access_token_validity, "
			+ "refresh_token_validity, additional_information, autoapprove";
	
	private static final String CLIENT_FIELDS = "client_secret, " + CLIENT_FIELDS_FOR_UPDATE;

	private static final String DEFAULT_INSERT_STATEMENT = "insert into oauth_client_details (" + CLIENT_FIELDS
			+ ", client_id) values (?,?,?,?,?,?,?,?,?,?,?)";

	private static final String DEFAULT_UPDATE_STATEMENT = "update oauth_client_details " + "set "
			+ CLIENT_FIELDS_FOR_UPDATE.replaceAll(", ", "=?, ") + "=? where client_id = ?";
	
	private static final String DEFAULT_UPDATE_SECRET_STATEMENT = "update oauth_client_details "
			+ "set client_secret = ? where client_id = ?";
	
	private static final String DEFAULT_DELETE_STATEMENT = "delete from oauth_client_details where client_id = ?";

	private String insertClientDetailsSql = DEFAULT_INSERT_STATEMENT;
	
	private String updateClientDetailsSql = DEFAULT_UPDATE_STATEMENT;

	private String updateClientSecretSql = DEFAULT_UPDATE_SECRET_STATEMENT;
	
	private String deleteClientDetailsSql = DEFAULT_DELETE_STATEMENT;

	@Autowired
	@Qualifier("masterJdbcTemplate")
	private JdbcTemplate masterJdbcTemplate;

	@Autowired
	@Qualifier("masterDataSource")
	private DataSource dataSource;

	public JdbcClientImpl(DataSource dataSource) {
		super(dataSource);
	}

	@Override
	public void addClientDetails(ClientDetails clientDetails) throws ClientAlreadyExistsException {
		try {
			BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
			masterJdbcTemplate.update(insertClientDetailsSql, encoder.encode(clientDetails.getClientSecret()),
					StringUtils.collectionToCommaDelimitedString(clientDetails.getResourceIds()),
					StringUtils.collectionToCommaDelimitedString(clientDetails.getScope()),
					StringUtils.collectionToCommaDelimitedString(clientDetails.getAuthorizedGrantTypes()),
					StringUtils.collectionToCommaDelimitedString(clientDetails.getRegisteredRedirectUri()),
					StringUtils.collectionToCommaDelimitedString(clientDetails.getAuthorities()),
					clientDetails.getAccessTokenValiditySeconds(), clientDetails.getRefreshTokenValiditySeconds(),
					clientDetails.getAdditionalInformation().toString(), "true", clientDetails.getClientId());
		} catch (DuplicateKeyException e) {
			throw new ClientAlreadyExistsException("Client already exists: " + clientDetails.getClientId(), e);
		}
	}

	@Override
	public void updateClientDetails(ClientDetails clientDetails) throws NoSuchClientException {
		String clientId = clientDetails.getClientId();
		ClientDetails client = this.loadClientByClientId(clientId);
		SummitClient convertClient = new SummitClient();
		convertClient = convertBean(convertClient, client);
		convertClient = convertBean(convertClient, clientDetails);
		int count = masterJdbcTemplate.update(updateClientDetailsSql,
				StringUtils.collectionToCommaDelimitedString(clientDetails.getResourceIds()),
				StringUtils.collectionToCommaDelimitedString(clientDetails.getScope()),
				StringUtils.collectionToCommaDelimitedString(clientDetails.getAuthorizedGrantTypes()),
				StringUtils.collectionToCommaDelimitedString(clientDetails.getRegisteredRedirectUri()),
				StringUtils.collectionToCommaDelimitedString(clientDetails.getAuthorities()),
				clientDetails.getAccessTokenValiditySeconds(), clientDetails.getRefreshTokenValiditySeconds(),
				clientDetails.getAdditionalInformation().toString(), "true", clientDetails.getClientId());
		if (count != 1) {
			throw new NoSuchClientException("No client found with id = " + clientDetails.getClientId());
		}
	}


	@Override
	public void updateClientSecret(String clientId, String secret) throws NoSuchClientException {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		int count = masterJdbcTemplate.update(updateClientSecretSql, encoder.encode(secret), clientId);
		if (count != 1) {
			throw new NoSuchClientException("No client found with id = " + clientId);
		}
	}

	@Override
	public void removeClientDetails(String clientId) throws NoSuchClientException {
		int count = masterJdbcTemplate.update(deleteClientDetailsSql, clientId);
		if (count != 1) {
			throw new NoSuchClientException("No client found with id = " + clientId);
		}
	}

	private SummitClient convertBean(SummitClient convertClient, ClientDetails client) {
		if (client.getAccessTokenValiditySeconds() != null) {
			convertClient.setAccessTokenValiditySeconds(client.getAccessTokenValiditySeconds());
		}
		if (client.getAdditionalInformation() != null
				&& "null".equalsIgnoreCase(client.getAdditionalInformation().toString())) {
			convertClient.setAdditionalInformation(client.getAdditionalInformation());
		}
		if (client.getAuthorities() != null) {
			Collection<GrantedAuthority> result = client.getAuthorities();
			List<Authority> authorList = new ArrayList<>();
			for (GrantedAuthority at : result) {
				Authority author = new Authority();
				author.setAuthName(at.getAuthority());
				authorList.add(author);
			}
			convertClient.setAuthorities(authorList);
		}
		if (client.getAuthorizedGrantTypes() != null) {
			convertClient.setAuthorizedGrantTypes(client.getAuthorizedGrantTypes());
		}
		if (client.getRefreshTokenValiditySeconds() != null) {
			convertClient.setRefreshTokenValiditySeconds(client.getRefreshTokenValiditySeconds());
		}
		if (client.getRegisteredRedirectUri() != null) {
			convertClient.setRegisteredRedirectUri(client.getRegisteredRedirectUri());
		}
		if (client.getResourceIds() != null) {
			convertClient.setResourceIds(client.getResourceIds());
		}
		if (client.getScope() != null) {
			convertClient.setScope(client.getScope());
		}
		return convertClient;
	}

}
