package com.central.server.oauth2;

import com.central.server.oauth2.client.RedisClientDetailsService;
import com.central.server.oauth2.code.RedisAuthorizationCodeServices;
import com.central.server.oauth2.properties.PermitUrlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.RandomValueAuthorizationCodeServices;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.AntPathMatcher;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

/**
 * @Description 资源服务器：定义资源的访问权限
 * @Author Derek
 * @Date 2018/10/15 15:00
 **/
@Configuration
@EnableResourceServer
@EnableConfigurationProperties(PermitUrlProperties.class)
public class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private static final String DEMO_RESOURCE_ID = "api-auth";
    @Autowired
    private PermitUrlProperties permitUrlProperties;

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/health");
        web.ignoring().antMatchers("/oauth/user/token");
        web.ignoring().antMatchers("/oauth/client/token");
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources.resourceId(DEMO_RESOURCE_ID).stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(
                /**
                 * 判断来源请求是否包含oauth2授权信息
                 */
                new RequestMatcher() {
                    private AntPathMatcher antPathMatcher = new AntPathMatcher();
                    @Override
                    public boolean matches(HttpServletRequest request) {
                        // 请求参数中包含access_token参数
                        if (request.getParameter(OAuth2AccessToken.ACCESS_TOKEN) != null) {
                            return true;
                        }
                        // 头部的Authorization值以Bearer开头
                        String auth = request.getHeader("Authorization");
                        if (auth != null) {
                            if (auth.startsWith(OAuth2AccessToken.BEARER_TYPE)) {
                                return true;
                            }
                        }

                        String url = request.getRequestURI() ;

                        if (antPathMatcher.match(request.getRequestURI(),  "/oauth/userinfo")) {
                            return true;
                        }
                        if (antPathMatcher.match(request.getRequestURI(),  "/oauth/remove/token")) {
                            return true;
                        }
                        if (antPathMatcher.match(request.getRequestURI(),  "/oauth/get/token")) {
                            return true;
                        }
                        if (antPathMatcher.match(request.getRequestURI(),  "/oauth/refresh/token")) {
                            return true;
                        }

                        if (antPathMatcher.match(request.getRequestURI(),  "/oauth/token/list")) {
                            return true;
                        }

                        if (antPathMatcher.match("/clients/**",  request.getRequestURI())) {
                            return true;
                        }

                        if (antPathMatcher.match("/services/**",  request.getRequestURI())) {
                            return true;
                        }
                        if (antPathMatcher.match("/redis/**",  request.getRequestURI())) {
                            return true;
                        }
                        return false;
                    }
                }

        ).authorizeRequests().antMatchers(permitUrlProperties.getOauth_urls()).permitAll().anyRequest() .authenticated();
    }

}
