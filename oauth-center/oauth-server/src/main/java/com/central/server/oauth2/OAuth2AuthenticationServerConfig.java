package com.central.server.oauth2;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.central.server.oauth2.token.store.RedisTemplateTokenStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.stereotype.Component;

import com.central.server.oauth2.client.RedisClientDetailsService;
import com.central.server.oauth2.code.RedisAuthorizationCodeServices;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author owen 624191343@qq.com
 * @version 创建时间：2017年11月12日 上午22:57:51
 */
@Configuration
public class OAuth2AuthenticationServerConfig {

	private Logger logger = LoggerFactory.getLogger(OAuth2AuthenticationServerConfig.class);

	@Resource
	private DataSource dataSource;

	@Resource
	private RedisTemplate<String, Object> redisTemplate;

	// 声明 ClientDetails实现
	@Bean
	@ConditionalOnProperty(prefix = "security.oauth2.token.store", name = "type", havingValue = "redis", matchIfMissing = true)
	public RedisClientDetailsService clientDetailsService() {
		RedisClientDetailsService clientDetailsService = new RedisClientDetailsService(dataSource);
		clientDetailsService.setRedisTemplate(redisTemplate);
		return clientDetailsService;
	}

	@Bean
	public RandomValueAuthorizationCodeServices authorizationCodeServices() {
		RedisAuthorizationCodeServices redisAuthorizationCodeServices = new RedisAuthorizationCodeServices();
		redisAuthorizationCodeServices.setRedisTemplate(redisTemplate);
		return redisAuthorizationCodeServices;
	}

	/**
	 * @Description DefaultTokenServices默认处理
	 *                          认证服务器： 管理token信息（生成，存储等），客户端信息（存储），认证服务器访问权限
	 * @Author Derek
	 * @Date 2018/10/15 14:58
	 **/
	@Component
	@Configuration
	@EnableAuthorizationServer
	@AutoConfigureAfter(AuthorizationServerEndpointsConfigurer.class)
	public class UnieapAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
		/**
		 * 注入authenticationManager 来支持 password grant type
		 */
		@Autowired
		private AuthenticationManager authenticationManager;

		@Autowired
		private UserDetailsService userDetailsService;

		@Autowired(required = false)
		private RedisTemplateTokenStore redisTokenStore;

		@Autowired(required = false)
		private JwtTokenStore jwtTokenStore;

		@Autowired(required = false)
		private JwtAccessTokenConverter jwtAccessTokenConverter;

		@Autowired
		private WebResponseExceptionTranslator webResponseExceptionTranslator;

		@Autowired
		private RedisClientDetailsService clientDetailsService;

		@Autowired(required = false)
		private RandomValueAuthorizationCodeServices authorizationCodeServices;

		// 配置身份认证器，配置认证方式，TokenStore，TokenGranter，OAuth2RequestFactory
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			if (jwtTokenStore != null) {
				endpoints.tokenStore(jwtTokenStore).authenticationManager(authenticationManager).userDetailsService(userDetailsService); // 支持
			} else if (redisTokenStore != null) {
				endpoints.tokenStore(redisTokenStore).authenticationManager(authenticationManager).userDetailsService(userDetailsService); // 支持
			}
			if (jwtAccessTokenConverter != null) {
				endpoints.accessTokenConverter(jwtAccessTokenConverter);
			}
			endpoints.authorizationCodeServices(authorizationCodeServices);
			endpoints.exceptionTranslator(webResponseExceptionTranslator);
		}


		/**
		 * 	配置应用名称 应用id
		 * 	配置OAuth2的客户端相关信息
		 * 	ClientDetailsServiceConfigurer 类可以配置多种实现，能够使用内存或 JDBC 方式实现获取已注册的客户端详情
		 * clientId：客户端标识 ID
		 * secret：客户端安全码
		 * scope：客户端访问范围，默认为空则拥有全部范围
		 * authorizedGrantTypes：客户端使用的授权类型，默认为空
		 * authorities：客户端可使用的权限
		 * @param clients
		 * @throws Exception
		 */
		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients.withClientDetails(clientDetailsService);
			clientDetailsService.loadAllClientToCache();
		}

		// 对应于配置AuthorizationServer安全认证的相关信息，创建ClientCredentialsTokenEndpointFilter核心过滤器
		//用来配置令牌端点(Token Endpoint)的安全约束.
		@Override
		public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

			security
					// 开启/oauth/token_key验证端口无权限访问
					.tokenKeyAccess("permitAll()")
					// 开启/oauth/check_token验证端口认证权限访问
					.checkTokenAccess("isAuthenticated()")
					// allow check token
					.allowFormAuthenticationForClients();

			// security.allowFormAuthenticationForClients();
			// security.tokenKeyAccess("permitAll()");
			// security.tokenKeyAccess("isAuthenticated()");
		}

	}


}
