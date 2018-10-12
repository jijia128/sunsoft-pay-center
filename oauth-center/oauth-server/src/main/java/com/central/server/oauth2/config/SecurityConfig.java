package com.central.server.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;

import com.central.model.properties.PermitUrlProperties;

/**
 * spring security配置
 *
 * @author owen 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51 2017年10月16日
 *          在WebSecurityConfigurerAdapter不拦截oauth要开放的资源
 */
@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(PermitUrlProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	// @Autowired
	// private LogoutSuccessHandler logoutSuccessHandler;

	@Autowired(required = false)
	private AuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private OauthLogoutHandler oauthLogoutHandler;

	@Autowired
	private PermitUrlProperties permitUrlProperties ;

	@Override
	public void configure(WebSecurity web) throws Exception {
		//健康监控
		web.ignoring().antMatchers("/health");
		//swagger访问
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources", "/configuration/security", "/swagger-ui.html", "/webjars/**", "/doc.html", "/login.html");
		//静态文件
		web.ignoring().antMatchers("/assets/**");
		// 忽略登录界面
		web.ignoring().antMatchers("/login.html");
		web.ignoring().antMatchers("/hello.html");
		//Token
		web.ignoring().antMatchers("/oauth/user/token");
		web.ignoring().antMatchers("/oauth/client/token");
	}
	/**
	 * 认证管理
	 *
	 * @return 认证管理对象
	 * @throws Exception
	 *             认证异常信息
	 */
	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//关闭跨域访问安全限制功能
		http.csrf().disable();
		//设置系统允许的URL
		http.authorizeRequests().antMatchers(permitUrlProperties.getHttp_urls() ).permitAll()
				.anyRequest().authenticated();
		//设置登录页面，登录处理地址，成功，失败的处理方法
		http.formLogin()
				.loginPage("/login.html")
				.loginProcessingUrl("/user/login")
				.successHandler(authenticationSuccessHandler)
				.failureHandler(authenticationFailureHandler);

		// 基于密码 等模式可以无session,不支持授权码模式
		if (authenticationEntryPoint != null) {
			http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		} else {
			// 授权码模式单独处理，需要session的支持，此模式可以支持所有oauth2的认证
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
		}
		//设置退出系统地址
		http.logout()
				.logoutUrl("/user/logout")
				.clearAuthentication(true)
				.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler())
				.addLogoutHandler(oauthLogoutHandler);

		// http.logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler);
		// 解决不允许显示在iframe的问题
		http.headers().frameOptions().disable();
		http.headers().cacheControl();

	}

	/**
	 * 全局用户信息
	 *
	 * @param auth
	 *            认证管理
	 * @throws Exception
	 *             用户认证异常信息
	 */
	@Autowired
	public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
	}


}
