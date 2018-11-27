package com.summit.config;

import javax.annotation.Resource;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.summit.filter.CustomAuthenticationFilter;
import com.summit.handle.LoginFailHandler;
import com.summit.handle.LoginSuccessHandler;
import com.summit.service.UserServiceDetail;


/**
 * 
 * @author yt
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(6)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	
	/* @Autowired
	 //注意这个方法是注入的 
	 public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception { 
		 auth.userDetailsService(userServiceDetail);
		 }
	*/
	  /**
     * 需要放行的URL
     */
    private static final String[] AUTH_WHITELIST = {
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/springboot/v2/api-docs",
            "/springboot/swagger-resources",
            "/springboot/swagger-resources/**",
            "/springboot/configuration/ui",
            "/springboot/configuration/security",
            "/springboot/swagger-ui.html"
            // other public endpoints of your API may be appended to this array
    };
    
	@Resource(name = "userServiceDetail")
	private UserServiceDetail userServiceDetail;
	@Bean
	LoginSuccessHandler loginSuccessHandler() {
		return new LoginSuccessHandler();
	}

	@Bean
	LoginFailHandler loginFailHandler() {
		return new LoginFailHandler();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		// 使用 BCrypt 加密
		return new BCryptPasswordEncoder(); 
	}


	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		AuthenticationManager manager = super.authenticationManagerBean();
		return manager;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.userServiceDetail).passwordEncoder(passwordEncoder());

	}

	/**
	 * 配置拦截器 处理login是json的情况
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean
	CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
		CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
		filter.setAuthenticationSuccessHandler(loginSuccessHandler());
		filter.setAuthenticationFailureHandler(loginFailHandler());
		
		// 这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
		filter.setAuthenticationManager(authenticationManagerBean());
		return filter;
	}


    @Override
    protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable().authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll()
                // /oauth/authorize link org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint
                // 必须登录过的用户才可以进行 oauth2 的授权码申请
                .antMatchers("/", "/home","/login","/oauth/authorize","/code/redirect").permitAll().anyRequest()
        		.authenticated()
                .and()
            .formLogin()
                .loginPage("/login").permitAll().successHandler(loginSuccessHandler()).failureUrl("/login-error?secError=true")
                .and().exceptionHandling().accessDeniedPage("/403")
                .and()
            .httpBasic()
                .disable()
            .exceptionHandling()
                .accessDeniedPage("/login?authorization_error=true")
                .and()
            // TODO: put CSRF protection back into this endpoint
            .csrf()
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
                .disable();
//                .loginPage("/login") .failureUrl("/login?authentication_error=true")   .httpBasic();            
		http.sessionManagement().maximumSessions(60);
		http.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
