package com.summit.config;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import com.summit.constants.SecurityConstants;
import com.summit.domain.user.UserBean;
import com.summit.handle.AuthExceptionEntryPoint;
import com.summit.handle.MyAccessDeniedHandler;

/**
 * 
 * @author yt
 *
 */
@Configurable
@Order(2)
public class OauthConfig {
	private static final String DEMO_RESOURCE_ID = "demo";

	
	@Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
		@Override
		public void configure(ResourceServerSecurityConfigurer resources) {
			resources.resourceId(DEMO_RESOURCE_ID).stateless(true);
			// 如果关闭 stateless，则 accessToken 使用时的 session id 会被记录，后续请求不携带
			// accessToken 也可以正常响应
			resources.authenticationEntryPoint(new AuthExceptionEntryPoint())
            .accessDeniedHandler(new MyAccessDeniedHandler());
		}

		@Override
		public void configure(HttpSecurity http) throws Exception {
			// 保险起见，防止被主过滤器链路拦截
			//http.requestMatchers().antMatchers("/api*/**").and().authorizeRequests().anyRequest().authenticated().and()
			//.authorizeRequests().antMatchers("/api*/**").access("#oauth2.hasScope('get_user_info')");

		
			http.authorizeRequests().antMatchers("/demo/**").access("#oauth2.hasScope('get_user_info')");
		}
	}

	@Configuration
	@EnableAuthorizationServer
	protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

		@Autowired
		@Qualifier("masterDataSource")
		DataSource dataSource;
		
		
		@Autowired
		@Qualifier("authenticationManagerBean")
		private AuthenticationManager authenticationManager;


		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

			// 访问数据库方式
			
			  JdbcClientDetailsService clientDetailsService = new
			  JdbcClientDetailsService(dataSource);
			  clientDetailsService.setSelectClientDetailsSql(SecurityConstants.
			  DEFAULT_SELECT_STATEMENT);
			  clientDetailsService.setFindClientDetailsSql(SecurityConstants.
			  DEFAULT_FIND_STATEMENT);
			  
			  clients.withClientDetails(clientDetailsService);
			 

			//clients.withClientDetails(esClientDetailService);
		}

		@Bean
		public ApprovalStore approvalStore() {
			TokenApprovalStore store = new TokenApprovalStore();
			store.setTokenStore(tokenStore());
			return store;
		}

		@Autowired
		RedisConnectionFactory redisConnectionFactory;

		@Bean
		public TokenStore tokenStore() {
			// return new InMemoryTokenStore();
			// 需要使用 redis 的话，放开这里
			return new RedisTokenStore(redisConnectionFactory);
		}

		
		 @Bean
		    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
		        redisTemplate.setConnectionFactory(redisConnectionFactory);

		        StringRedisSerializer serializer = new StringRedisSerializer();
		        redisTemplate.setKeySerializer(serializer);
		        redisTemplate.setValueSerializer(serializer);
		        return redisTemplate;
		    }
		 
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) {

			/*
			 * endpoints.tokenStore(tokenStore()).authenticationManager(
			 * authenticationManager)
			 * .allowedTokenEndpointRequestMethods(HttpMethod.GET,
			 * HttpMethod.POST);
			 */

			// token增强配置
			TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
			tokenEnhancerChain.setTokenEnhancers(Arrays.asList(tokenEnhancer(), accessTokenConverter()));
			endpoints.tokenStore(tokenStore()).tokenEnhancer(tokenEnhancerChain)
					.authenticationManager(authenticationManager)
					.allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST);

		}

		@Bean
		public TokenEnhancer accessTokenConverter() {
			SummitJwtAccessTokenConverter jwtAccessTokenConverter = new SummitJwtAccessTokenConverter();
			jwtAccessTokenConverter.setSigningKey("summit");
			return jwtAccessTokenConverter;
		}

		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
			oauthServer.realm(DEMO_RESOURCE_ID).allowFormAuthenticationForClients();
		}

		@Bean
		public TokenEnhancer tokenEnhancer() {
			return (accessToken, authentication) -> {
				final Map<String, Object> additionalInfo = new HashMap<>(2);
				additionalInfo.put("license", SecurityConstants.SUMMIT_LICENSE);
				UserBean user = (UserBean) authentication.getUserAuthentication().getPrincipal();
				if (user != null) {
					//System.out.println(user.toString());
					additionalInfo.put("username", user.getUsername());
				}
				((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
				return accessToken;
			};
		}

	}

}
