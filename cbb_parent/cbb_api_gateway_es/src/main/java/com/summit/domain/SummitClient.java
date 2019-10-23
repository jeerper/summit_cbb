package com.summit.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

/**
 * @author yt
 */
public class SummitClient implements ClientDetails {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String clientId;
    private Set<String> resourceIds;
    private String clientSecret;
    private Set<String> scope;
    private Set<String> authorizedGrantTypes;
    private Set<String> registeredRedirectUri;

    private List<Authority> authorities;

    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;
    private Map<String, Object> additionalInformation;


    public void setScope(Set<String> scope) {
        this.scope = scope;
    }

    @Override
    public Set<String> getScope() {
        return scope;
    }


    @Override
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Set<String> getResourceIds() {
        return resourceIds;
    }

    public void setResourceIds(Set<String> resourceIds) {
        this.resourceIds = resourceIds;
    }

    @Override
    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
		  /*PasswordEncoder  encoder = new BCryptPasswordEncoder();
	        String enclientSecret = encoder.encode(clientSecret);*/
        this.clientSecret = clientSecret;
    }

    @Override
    public Set<String> getAuthorizedGrantTypes() {
        return authorizedGrantTypes;
    }

    public void setAuthorizedGrantTypes(Set<String> authorizedGrantTypes) {
        this.authorizedGrantTypes = authorizedGrantTypes;
    }

    @Override
    public Set<String> getRegisteredRedirectUri() {
        return registeredRedirectUri;
    }

    public void setRegisteredRedirectUri(Set<String> registeredRedirectUri) {
        this.registeredRedirectUri = registeredRedirectUri;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> result = new ArrayList<GrantedAuthority>();
        for (Authority at : authorities) {
            result.add(new SimpleGrantedAuthority(at.getAuthority()));
        }
        return result;
    }

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Integer getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds() {
        return refreshTokenValiditySeconds;
    }

    public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
        this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
    }

    @Override
    public Map<String, Object> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(Map<String, Object> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    @Override
    public boolean isSecretRequired() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isScoped() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isAutoApprove(String scope) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public String toString() {
        return "SummitClient [clientId=" + clientId + ", resourceIds=" + resourceIds + ", clientSecret=" + clientSecret
                + ", scope=" + scope + ", authorizedGrantTypes=" + authorizedGrantTypes + ", registeredRedirectUri="
                + registeredRedirectUri + ", authorities=" + authorities + ", accessTokenValiditySeconds="
                + accessTokenValiditySeconds + ", refreshTokenValiditySeconds=" + refreshTokenValiditySeconds
                + ", additionalInformation=" + additionalInformation + "]";
    }


}
