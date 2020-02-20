package com.summit.config;


import com.summit.common.constant.CommonConstant;
import com.summit.model.user.UserBean;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;


public class SummitJwtAccessTokenConverter extends JwtAccessTokenConverter {
   @Override
   public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
       final Map<String, Object> additionalInfo = new HashMap<>(2);
       UserBean user = (UserBean) authentication.getUserAuthentication().getPrincipal();
       if (user != null) {
           additionalInfo.put("username", user.getUsername());
       }
       additionalInfo.put("license", CommonConstant.LICENSE);
       ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
       return super.enhance(accessToken,  authentication);
   }
}
