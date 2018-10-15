
package com.central.client;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.expression.OAuth2WebSecurityExpressionHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.central.client.oauth2.authorize.AuthorizeConfigManager;
import com.central.client.oauth2.properties.PermitUrlProperties;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 资源服务器
 * @author 作者 Derek
 * @version 创建时间：2017年11月12日 上午22:57:51
 */
//容器管理
@Component
//配置类
@Configuration
//资源服务器
@EnableResourceServer
// 开启spring security 注解
@EnableGlobalMethodSecurity(prePostEnabled = true)
//使属性配置类生效
@EnableConfigurationProperties(PermitUrlProperties.class)
public class OAuth2ClientConfig extends ResourceServerConfigurerAdapter{

	//对应oauth_client_details的 resource_ids字段 如果表中有数据 client_id只能访问响应resource_ids的资源服务器
	private static final String DEMO_RESOURCE_ID = "api-user";

	/**
	 * SpringMvc启动时自动装配json处理类
	 */
	@Resource
	private ObjectMapper objectMapper ;

	/**
	 *  Redis存储Token，
	 *  不是必须的，满足配置条件时，自动装配进来
	 */
	@Autowired(required = false)
	private TokenStore redisTokenStore;

	/**
	 *  JWT存储Token，
	 *  不是必须的，满足配置条件时，自动装配进来
	 */
	@Autowired(required = false)
	private JwtTokenStore jwtTokenStore;

	/**
	 *  JWT定义Token的生成方式
	 *  不是必须的，满足配置条件时，自动装配进来
	 */
	@Autowired(required = false)
	private JwtAccessTokenConverter jwtAccessTokenConverter;

	/**
	 *  定义认证处理的相应方式。
	 *  AuthenticationEntryPoint 用来解决匿名用户访问无权限资源时的异常
	 * AccessDeineHandler 用来解决认证过的用户访问无权限资源时的异常
	 */
	@Autowired
	private AuthenticationEntryPoint authenticationEntryPoint;

	/**
	 * 认证失败处理
	 */
	@Autowired
	private AuthenticationFailureHandler authenticationFailureHandler;

	/**
	 * 自定义类
	 * 认证配置接口
	 */
	@Autowired
	private AuthorizeConfigManager authorizeConfigManager;

	/**
	 *
	 */
	@Autowired
	private OAuth2WebSecurityExpressionHandler expressionHandler;

	/**
	 * 访问被拒绝处理方法
	 */
	@Autowired
	private OAuth2AccessDeniedHandler oAuth2AccessDeniedHandler;

	/**
	 * 不需要权限验证的URL
	 */
	@Autowired
	private PermitUrlProperties permitUrlProperties;

	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(permitUrlProperties.getHttp_urls());
	}

	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

		if (jwtTokenStore != null) {
			resources.tokenStore(jwtTokenStore);
		} else if (redisTokenStore != null) {
			resources.tokenStore(redisTokenStore);
		}
		resources.stateless(true);
		resources.authenticationEntryPoint(authenticationEntryPoint);
		resources.expressionHandler(expressionHandler);
		resources.accessDeniedHandler(oAuth2AccessDeniedHandler);
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		//禁用跨域访问现在
		http.csrf().disable();
		http.headers().frameOptions().disable();

		authorizeConfigManager.config(http.authorizeRequests());
	}

}
